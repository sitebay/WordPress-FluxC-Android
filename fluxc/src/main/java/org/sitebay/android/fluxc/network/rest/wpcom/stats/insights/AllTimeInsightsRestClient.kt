package org.sitebay.android.fluxc.network.rest.wpcom.stats.insights

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
import org.sitebay.android.fluxc.network.rest.wpcom.stats.time.StatsUtils
import org.sitebay.android.fluxc.store.StatsStore.FetchStatsPayload
import org.sitebay.android.fluxc.store.toStatsError
import java.util.Date
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AllTimeInsightsRestClient @Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent,
    val statsUtils: StatsUtils
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchAllTimeInsights(site: SiteModel, forced: Boolean): FetchStatsPayload<AllTimeResponse> {
        val url = WPCOMREST.sites.site(site.siteId).stats.urlV1_1

        val params = mapOf<String, String>()
        val response = wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                params,
                AllTimeResponse::class.java,
                enableCaching = true,
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

    data class AllTimeResponse(
        @SerializedName("date") var date: Date? = null,
        @SerializedName("stats") val stats: StatsResponse?
    ) {
        data class StatsResponse(
            @SerializedName("visitors") val visitors: Int?,
            @SerializedName("views") val views: Int?,
            @SerializedName("posts") val posts: Int?,
            @SerializedName("views_best_day") val viewsBestDay: String?,
            @SerializedName("views_best_day_total") val viewsBestDayTotal: Int?
        )
    }
}
