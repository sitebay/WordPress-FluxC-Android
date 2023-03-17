package org.wordpress.android.fluxc.network.rest.wpapi.site

import com.android.volley.RequestQueue
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.network.BaseRequest.BaseNetworkError
import org.wordpress.android.fluxc.network.BaseRequest.GenericErrorType
import org.wordpress.android.fluxc.network.UserAgent
import org.wordpress.android.fluxc.network.discovery.DiscoveryUtils
import org.wordpress.android.fluxc.network.discovery.RootWPAPIRestResponse
import org.wordpress.android.fluxc.network.rest.wpapi.BaseWPAPIRestClient
import org.wordpress.android.fluxc.network.rest.wpapi.Nonce.Available
import org.wordpress.android.fluxc.network.rest.wpapi.Nonce.FailedRequest
import org.wordpress.android.fluxc.network.rest.wpapi.CookieNonceAuthenticator
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIGsonRequestBuilder
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPINetworkError
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIResponse.Error
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIResponse.Success
import org.wordpress.android.fluxc.store.SiteStore.FetchWPAPISitePayload
import org.wordpress.android.util.UrlUtils
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SiteWPAPIRestClient @Inject constructor(
    private val cookieNonceAuthenticator: CookieNonceAuthenticator,
    private val wpapiGsonRequestBuilder: WPAPIGsonRequestBuilder,
    dispatcher: Dispatcher,
    @Named("custom-ssl") requestQueue: RequestQueue,
    userAgent: UserAgent
) : BaseWPAPIRestClient(dispatcher, requestQueue, userAgent) {
    companion object {
        private const val WOO_API_NAMESPACE_PREFIX = "wc/"
        private const val FETCH_API_CALL_FIELDS =
            "name,description,gmt_offset,url,authentication,namespaces"
    }

    suspend fun fetchWPAPISite(
        payload: FetchWPAPISitePayload
    ): SiteModel {
        val cleanedUrl = UrlUtils.addUrlSchemeIfNeeded(payload.url, false).let { urlWithScheme ->
            DiscoveryUtils.stripKnownPaths(urlWithScheme)
        }
        var discoveredWpApiUrl: String? = null

        val result = cookieNonceAuthenticator.makeAuthenticatedWPAPIRequest(
            siteUrl = cleanedUrl,
            username = payload.username,
            password = payload.password
        ) { wpApiUrl, nonce ->
            if (nonce !is Available) {
                val networkError = (nonce as? FailedRequest)?.networkError ?: BaseNetworkError(GenericErrorType.UNKNOWN)

                return@makeAuthenticatedWPAPIRequest Error(WPAPINetworkError(networkError))
            }

            discoveredWpApiUrl = wpApiUrl
            wpapiGsonRequestBuilder.syncGetRequest(
                restClient = this,
                url = wpApiUrl,
                clazz = RootWPAPIRestResponse::class.java,
                params = mapOf("_fields" to FETCH_API_CALL_FIELDS),
                nonce = nonce.value
            )
        }

        return when (result) {
            is Success -> {
                val response = result.data
                SiteModel().apply {
                    name = response?.name
                    description = response?.description
                    timezone = response?.gmtOffset
                    origin = SiteModel.ORIGIN_WPAPI
                    hasWooCommerce = response?.namespaces?.any {
                        it.startsWith(WOO_API_NAMESPACE_PREFIX)
                    } ?: false
                    wpApiRestUrl = discoveredWpApiUrl
                    this.url = response?.url ?: cleanedUrl
                    this.username = payload.username
                    this.password = payload.password
                }
            }

            is Error -> {
                SiteModel().apply {
                    error = result.error
                }
            }
        }
    }

    suspend fun fetchWPAPISite(
        site: SiteModel
    ): SiteModel {
        return fetchWPAPISite(
            payload = FetchWPAPISitePayload(
                username = site.username,
                password = site.password,
                url = site.url
            )
        )
    }
}
