package org.sitebay.android.fluxc.store

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.XPostSiteModel
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response
import org.sitebay.android.fluxc.network.rest.wpcom.site.XPostsRestClient
import org.sitebay.android.fluxc.persistence.XPostsSqlUtils
import org.sitebay.android.fluxc.store.XPostsSource.DB
import org.sitebay.android.fluxc.store.XPostsSource.REST_API
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject

class XPostsStore
@Inject constructor(
    private val coroutineEngine: CoroutineEngine,
    private val xPostsRestClient: XPostsRestClient,
    private val xPostsSqlUtils: XPostsSqlUtils
) {
    suspend fun fetchXPosts(site: SiteModel): XPostsResult =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchXPosts") {
                return@withDefaultContext when (val response = xPostsRestClient.fetch(site)) {
                    is Response.Success -> {
                        val xPosts = response.data.toList()
                        xPostsSqlUtils.setXPostsForSite(xPosts, site)
                        XPostsResult.apiResult(xPosts)
                    }
                    is Response.Error -> when (response.error.apiError) {
                        "unauthorized",
                        "xposts_require_o2_enabled" -> {
                            // These errors mean the site does not support xposts for this user
                            xPostsSqlUtils.persistNoXpostsForSite(site)
                            XPostsResult.apiResult(emptyList())
                        }
                        else -> {
                            // Call failed for unknown reason, leave db unchanged and return saved data
                            savedXPosts(site)
                        }
                    }
                }
            }

    suspend fun getXPostsFromDb(site: SiteModel): XPostsResult =
        coroutineEngine.withDefaultContext(AppLog.T.DB, this, "getXPostsFromDb") {
            return@withDefaultContext savedXPosts(site)
        }

    /**
     * Returns either (a) a list of XPosts from the db wrapped in an [XPostsResult.Result], or
     * (b) [XPostsResult.Unknown] if we have never gotten a response from the backend indicating
     * whether this site has any XPosts.
     */
    private fun savedXPosts(site: SiteModel) =
        xPostsSqlUtils.selectXPostsForSite(site)?.let {
            XPostsResult.dbResult(it)
        } ?: XPostsResult.Unknown
}

enum class XPostsSource { REST_API, DB }

sealed class XPostsResult {
    data class Result(val xPosts: List<XPostSiteModel>, val source: XPostsSource) : XPostsResult()
    object Unknown : XPostsResult()
    companion object {
        fun dbResult(xPosts: List<XPostSiteModel>) = Result(xPosts, DB)
        fun apiResult(xPosts: List<XPostSiteModel>) = Result(xPosts, REST_API)
    }
}
