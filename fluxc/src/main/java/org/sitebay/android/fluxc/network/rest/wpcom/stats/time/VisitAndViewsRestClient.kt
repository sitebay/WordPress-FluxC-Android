package org.sitebay.android.fluxc.network.rest.wpcom.stats.time

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
import org.sitebay.android.fluxc.network.utils.StatsGranularity
import org.sitebay.android.fluxc.store.StatsStore.FetchStatsPayload
import org.sitebay.android.fluxc.store.toStatsError
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class VisitAndViewsRestClient
@Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchVisits(
        site: SiteModel,
        granularity: StatsGranularity,
        date: String,
        itemsToLoad: Int,
        forced: Boolean
    ): FetchStatsPayload<VisitsAndViewsResponse> {
        val url = WPCOMREST.sites.site(site.siteId).stats.visits.urlV1_1
        val params = mapOf(
                "unit" to granularity.toString(),
                "quantity" to itemsToLoad.toString(),
                "date" to date
        )
        val response = wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                params,
                VisitsAndViewsResponse::class.java,
                enableCaching = false,
                forced = forced
        )
        return when (response) {
            is Success -> {
                FetchStatsPayload(response.data)
            }
            is Error -> {
                FetchStatsPayload(response.error.toStatsError())
            }
        }
    }

    data class VisitsAndViewsResponse(
        @SerializedName("date") val date: String?,
        @SerializedName("fields") val fields: List<String>?,
        @SerializedName("data") val data: List<List<String>?>?,
        @SerializedName("unit") val unit: String?
    )
}
