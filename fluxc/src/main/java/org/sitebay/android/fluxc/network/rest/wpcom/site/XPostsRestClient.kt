package org.sitebay.android.fluxc.network.rest.wpcom.site

import android.content.Context
import com.android.volley.RequestQueue
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WPCOMV2
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.XPostSiteModel
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class XPostsRestClient @Inject constructor(
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    dispatcher: Dispatcher,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetch(site: SiteModel): WPComGsonRequestBuilder.Response<Array<XPostSiteModel>> {
        val url = WPCOMV2.sites.site(site.siteId).xposts.url
        return wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                mapOf("decode_html" to "true"),
                Array<XPostSiteModel>::class.java,
                true,
                60 * 1000
        )
    }
}
