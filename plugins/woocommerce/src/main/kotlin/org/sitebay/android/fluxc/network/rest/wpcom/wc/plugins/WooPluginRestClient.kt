package org.sitebay.android.fluxc.network.rest.wpcom.wc.plugins

import android.content.Context
import com.android.volley.RequestQueue
import com.google.gson.annotations.SerializedName
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WPCOMREST
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Error
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Success
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooPayload
import org.sitebay.android.fluxc.network.rest.wpcom.wc.toWooError
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WooPluginRestClient @Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchInstalledPlugins(
        site: SiteModel
    ): WooPayload<FetchPluginsResponse> {
        val url = WPCOMREST.sites.site(site.siteId).plugins.urlV1_2

        val response = wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                emptyMap(),
                FetchPluginsResponse::class.java
        )
        return when (response) {
            is Success -> {
                WooPayload(response.data)
            }
            is Error -> {
                WooPayload(response.error.toWooError())
            }
        }
    }

    data class FetchPluginsResponse(val plugins: List<PluginModel>) {
        data class PluginModel(
            val slug: String,
            val version: String,
            @SerializedName("active") val isActive: Boolean,
            @SerializedName("display_name") val displayName: String
        )
    }
}
