package org.sitebay.android.fluxc.store.stats.insights

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.stats.InsightsMapper
import org.sitebay.android.fluxc.model.stats.insights.PostingActivityModel.Day
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.PostingActivityRestClient
import org.sitebay.android.fluxc.persistence.InsightsSqlUtils.PostingActivitySqlUtils
import org.sitebay.android.fluxc.store.StatsStore.OnStatsFetched
import org.sitebay.android.fluxc.store.StatsStore.StatsError
import org.sitebay.android.fluxc.store.StatsStore.StatsErrorType.INVALID_RESPONSE
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog.T.STATS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostingActivityStore
@Inject constructor(
    private val restClient: PostingActivityRestClient,
    private val sqlUtils: PostingActivitySqlUtils,
    private val coroutineEngine: CoroutineEngine,
    private val mapper: InsightsMapper
) {
    suspend fun fetchPostingActivity(
        site: SiteModel,
        startDay: Day,
        endDay: Day,
        forced: Boolean = false
    ) = coroutineEngine.withDefaultContext(STATS, this, "fetchPostingActivity") {
        if (!forced && sqlUtils.hasFreshRequest(site)) {
            return@withDefaultContext OnStatsFetched(getPostingActivity(site, startDay, endDay), cached = true)
        }
        val payload = restClient.fetchPostingActivity(site, startDay, endDay, forced)
        return@withDefaultContext when {
            payload.isError -> OnStatsFetched(payload.error)
            payload.response != null -> {
                sqlUtils.insert(site, payload.response)
                OnStatsFetched(mapper.map(payload.response, startDay, endDay))
            }
            else -> OnStatsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getPostingActivity(site: SiteModel, startDay: Day, endDay: Day) =
            coroutineEngine.run(STATS, this, "getPostingActivity") {
                sqlUtils.select(site)?.let { mapper.map(it, startDay, endDay) }
            }
}
