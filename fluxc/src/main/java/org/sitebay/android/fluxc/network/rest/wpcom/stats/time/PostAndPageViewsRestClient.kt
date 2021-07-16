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
import java.util.Date
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PostAndPageViewsRestClient
@Inject constructor(
    dispatcher: Dispatcher,
    private val wpComGsonRequestBuilder: WPComGsonRequestBuilder,
    appContext: Context?,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent,
    private val statsUtils: StatsUtils
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchPostAndPageViews(
        site: SiteModel,
        granularity: StatsGranularity,
        date: Date,
        pageSize: Int,
        forced: Boolean
    ): FetchStatsPayload<PostAndPageViewsResponse> {
        val url = WPCOMREST.sites.site(site.siteId).stats.top_posts.urlV1_1
        val params = mapOf(
                "period" to granularity.toString(),
                "max" to pageSize.toString(),
                "date" to statsUtils.getFormattedDate(date)
        )
        val response = wpComGsonRequestBuilder.syncGetRequest(
                this,
                url,
                params,
                PostAndPageViewsResponse::class.java,
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

    data class PostAndPageViewsResponse(
        @SerializedName("date") var date: Date? = null,
        @SerializedName("days") val days: Map<String, ViewsResponse>,
        @SerializedName("period") val statsGranularity: String?
    ) {
        data class ViewsResponse(
            @SerializedName("postviews") val postViews: List<PostViewsResponse>,
            @SerializedName("total_views") val totalViews: Int?
        ) {
            data class PostViewsResponse(
                @SerializedName("id") val id: Long?,
                @SerializedName("title") val title: String?,
                @SerializedName("type") val type: String?,
                @SerializedName("href") val href: String?,
                @SerializedName("views") val views: Int?
            )
        }
    }
}
