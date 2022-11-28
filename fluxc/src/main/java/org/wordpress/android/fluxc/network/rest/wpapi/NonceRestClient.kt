package org.wordpress.android.fluxc.network.rest.wpapi

import com.android.volley.NoConnectionError
import com.android.volley.RequestQueue
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.network.UserAgent
import org.wordpress.android.fluxc.network.rest.wpapi.Nonce.Available
import org.wordpress.android.fluxc.network.rest.wpapi.Nonce.FailedRequest
import org.wordpress.android.fluxc.network.rest.wpapi.Nonce.Unknown
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIResponse.Error
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIResponse.Success
import org.wordpress.android.fluxc.utils.CurrentTimeProvider
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NonceRestClient
@Inject constructor(
    private val wpApiEncodedBodyRequestBuilder: WPAPIEncodedBodyRequestBuilder,
    private val currentTimeProvider: CurrentTimeProvider,
    dispatcher: Dispatcher,
    @Named("custom-ssl") requestQueue: RequestQueue,
    userAgent: UserAgent
) : BaseWPAPIRestClient(dispatcher, requestQueue, userAgent) {
    private val nonceMap: MutableMap<String, Nonce> = mutableMapOf()
    fun getNonce(siteUrl: String): Nonce? = nonceMap[siteUrl]
    fun getNonce(site: SiteModel): Nonce? = nonceMap[site.url]

    /**
     *  Requests a nonce using the
     *  [rest-nonce endpoint](https://developer.wordpress.org/reference/functions/wp_ajax_rest_nonce/)
     *  that became available in WordPress 5.3.
     */
    suspend fun requestNonce(site: SiteModel): Nonce? {
        return requestNonce(site.url, site.username, site.password)
    }

    /**
     *  Requests a nonce using the
     *  [rest-nonce endpoint](https://developer.wordpress.org/reference/functions/wp_ajax_rest_nonce/)
     *  that became available in WordPress 5.3.
     */
    suspend fun requestNonce(siteUrl: String, username: String, password: String): Nonce? {
        val wpLoginUrl = slashJoin(siteUrl, "wp-login.php")
        val redirectUrl = slashJoin(siteUrl, "wp-admin/admin-ajax.php?action=rest-nonce")
        val body = mapOf(
            "log" to username,
            "pwd" to password,
            "redirect_to" to redirectUrl
        )
        val response =
            wpApiEncodedBodyRequestBuilder.syncPostRequest(this, wpLoginUrl, body = body)
        nonceMap[siteUrl] = when (response) {
            is Success -> if (response.data?.matches("[0-9a-zA-Z]{2,}".toRegex()) == true) {
                Available(response.data)
            } else {
                FailedRequest(currentTimeProvider.currentDate().time)
            }
            is Error -> {
                if (response.error.volleyError is NoConnectionError) {
                    // No connection, so we do not know if a nonce is available
                    Unknown
                } else {
                    FailedRequest(currentTimeProvider.currentDate().time)
                }
            }
        }
        return nonceMap[siteUrl]
    }

    private fun slashJoin(begin: String, end: String): String {
        val noSlashBegin = begin.replace("/$".toRegex(), "")
        val noSlashEnd = end.replace("^/".toRegex(), "")
        return "$noSlashBegin/$noSlashEnd"
    }
}
