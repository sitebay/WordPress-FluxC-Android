package org.sitebay.android.fluxc.network.rest.wpcom.wc.leaderboards

import android.content.Context
import com.android.volley.RequestQueue
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.generated.endpoint.WOOCOMMERCE
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.BaseWPComRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.sitebay.android.fluxc.network.rest.wpcom.jetpacktunnel.JetpackTunnelGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.wc.orderstats.OrderStatsRestClient.OrderStatsApiUnit
import org.sitebay.android.fluxc.store.WCStatsStore.StatsGranularity
import org.sitebay.android.fluxc.utils.DateUtils
import org.sitebay.android.fluxc.utils.handleResult
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LeaderboardsRestClient @Inject constructor(
    appContext: Context?,
    dispatcher: Dispatcher,
    @Named("regular") requestQueue: RequestQueue,
    accessToken: AccessToken,
    userAgent: UserAgent,
    private val jetpackTunnelGsonRequestBuilder: JetpackTunnelGsonRequestBuilder
) : BaseWPComRestClient(appContext, dispatcher, requestQueue, accessToken, userAgent) {
    suspend fun fetchLeaderboards(
        site: SiteModel,
        unit: StatsGranularity?,
        queryTimeRange: LongRange?,
        quantity: Int?
    ) = WOOCOMMERCE.leaderboards.pathV4Analytics
            .requestTo(site, unit, queryTimeRange, quantity)
            .handleResult()

    private suspend fun String.requestTo(
        site: SiteModel,
        unit: StatsGranularity?,
        queryTimeRange: LongRange?,
        quantity: Int?
    ) = jetpackTunnelGsonRequestBuilder.syncGetRequest(
            this@LeaderboardsRestClient,
            site,
            this,
            createParameters(site, unit, queryTimeRange, quantity),
            Array<LeaderboardsApiResponse>::class.java
    )

    private fun createParameters(
        site: SiteModel,
        unit: StatsGranularity?,
        queryTimeRange: LongRange?,
        quantity: Int?
    ) = mapOf(
            "before" to (
                    queryTimeRange?.endInclusive
                            ?: DateUtils.getEndDateForSite(site))
                    .toString(),
            "after" to (
                    queryTimeRange?.start
                            ?: unit?.startDateTime(site)
                            ?: "")
                    .toString(),
            "per_page" to quantity?.toString().orEmpty(),
            "interval" to (unit?.let { OrderStatsApiUnit.fromStatsGranularity(it).toString() } ?: "")
    ).filter { it.value.isNotEmpty() }
}
