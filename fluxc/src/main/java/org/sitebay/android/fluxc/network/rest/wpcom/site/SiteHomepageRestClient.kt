package org.sitebay.android.fluxc.network.rest.wpcom.site

import android.content.Context
import com.android.volley.RequestQueue
import com.google.gson.annotations.SerializedName
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WPCOMREST
import org.sitebay.android.fluxc.model.SiteHomepageSettings
import org.sitebay.android.fluxc.model.SiteHomepageSettings.ShowOnFront
import org.sitebay.android.fluxc.model.SiteHomepageSettings.StaticPage
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SiteHomepageRestClient
@Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun updateHomepage(
        site: SiteModel,
        homepageSettings: SiteHomepageSettings
    ): Response<UpdateHomepageResponse> {
        val url = WPCOMREST.sites.site(site.siteId).homepage.urlV1_1
        val body = mutableMapOf(
                "is_page_on_front" to (homepageSettings.showOnFront == ShowOnFront.PAGE).toString()
        )
        if (homepageSettings is StaticPage) {
            if (homepageSettings.pageOnFrontId > -1) {
                body["page_on_front_id"] = homepageSettings.pageOnFrontId.toString()
            }
            if (homepageSettings.pageForPostsId > -1) {
                body["page_for_posts_id"] = homepageSettings.pageForPostsId.toString()
            }
        }
        return wpComGsonRequestBuilder.syncPostRequest(
                this,
                url,
                null,
                body,
                UpdateHomepageResponse::class.java
        )
    }

    data class UpdateHomepageResponse(
        @SerializedName("is_page_on_front") val isPageOnFront: Boolean,
        @SerializedName("page_on_front_id") val pageOnFrontId: Long?,
        @SerializedName("page_for_posts_id") val pageForPostsId: Long?
    )
}
