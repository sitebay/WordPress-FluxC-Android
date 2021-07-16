package org.sitebay.android.fluxc.network.rest.wpcom.whatsnew

import android.content.Context
import com.android.volley.RequestQueue
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WPCOMV2
import org.sitebay.android.fluxc.model.whatsnew.WhatsNewAnnouncementModel
import org.sitebay.android.fluxc.model.whatsnew.WhatsNewAnnouncementModel.WhatsNewAnnouncementFeature
import org.sitebay.android.fluxc.network.Response
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Success
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.sitebay.android.fluxc.network.rest.wpcom.whatsnew.WhatsNewRestClient.WhatsNewResponse.Announcement
import org.sitebay.android.fluxc.store.WhatsNewStore.WhatsNewAppId
import org.sitebay.android.fluxc.store.WhatsNewStore.WhatsNewFetchedPayload
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WhatsNewRestClient @Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchWhatsNew(versionName: String, appId: WhatsNewAppId): WhatsNewFetchedPayload {
        val url = WPCOMV2.mobile.feature_announcements.url

        val params = mapOf(
                "app_id" to appId.id.toString(),
                "app_version" to versionName
        )

        val response = wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                params,
                WhatsNewResponse::class.java,
                enableCaching = false,
                forced = true
        )

        return when (response) {
            is Success -> {
                val announcements = response.data.announcements
                buildWhatsNewPayload(announcements)
            }
            is WPComGsonRequestBuilder.Response.Error -> {
                val payload = WhatsNewFetchedPayload()
                payload.error = response.error
                payload
            }
        }
    }

    private fun buildWhatsNewPayload(
        announcements: List<Announcement>?
    ): WhatsNewFetchedPayload {
        return WhatsNewFetchedPayload(announcements?.map { announce ->
            WhatsNewAnnouncementModel(
                    appVersionName = announce.appVersionName,
                    announcementVersion = announce.announcementVersion,
                    minimumAppVersion = announce.minimumAppVersion,
                    maximumAppVersion = announce.maximumAppVersion,
                    appVersionTargets = announce.appVersionTargets ?: emptyList(),
                    detailsUrl = announce.detailsUrl,
                    isLocalized = announce.isLocalized,
                    responseLocale = announce.responseLocale,
                    features = announce.features.map {
                        WhatsNewAnnouncementFeature(
                                title = it.title,
                                subtitle = it.subtitle,
                                iconBase64 = it.iconBase64,
                                iconUrl = it.iconUrl
                        )
                    }
            )
        })
    }

    data class WhatsNewResponse(
        val announcements: List<Announcement>?
    ) : Response {
        data class Announcement(
            val appVersionName: String,
            val announcementVersion: Int,
            val minimumAppVersion: String,
            val maximumAppVersion: String,
            val appVersionTargets: List<String>?,
            val detailsUrl: String,
            val isLocalized: Boolean,
            val responseLocale: String,
            val features: List<Feature>
        )

        data class Feature(
            val title: String,
            val subtitle: String,
            val iconBase64: String,
            val iconUrl: String
        )
    }
}
