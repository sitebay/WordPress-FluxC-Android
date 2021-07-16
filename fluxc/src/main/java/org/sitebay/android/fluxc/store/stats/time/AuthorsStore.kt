package org.sitebay.android.fluxc.store.stats.time

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.stats.LimitMode
import org.sitebay.android.fluxc.model.stats.LimitMode.Top
import org.sitebay.android.fluxc.model.stats.time.TimeStatsMapper
import org.sitebay.android.fluxc.network.rest.wpcom.stats.time.AuthorsRestClient
import org.sitebay.android.fluxc.network.utils.StatsGranularity
import org.sitebay.android.fluxc.persistence.TimeStatsSqlUtils.AuthorsSqlUtils
import org.sitebay.android.fluxc.store.StatsStore.OnStatsFetched
import org.sitebay.android.fluxc.store.StatsStore.StatsError
import org.sitebay.android.fluxc.store.StatsStore.StatsErrorType.INVALID_RESPONSE
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog.T.STATS
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorsStore
@Inject constructor(
    private val restClient: AuthorsRestClient,
    private val sqlUtils: AuthorsSqlUtils,
    private val timeStatsMapper: TimeStatsMapper,
    private val coroutineEngine: CoroutineEngine
) {
    suspend fun fetchAuthors(
        site: SiteModel,
        period: StatsGranularity,
        limitMode: Top,
        date: Date,
        forced: Boolean = false
    ) = coroutineEngine.withDefaultContext(STATS, this, "fetchAuthors") {
        if (!forced && sqlUtils.hasFreshRequest(site, period, date, limitMode.limit)) {
            return@withDefaultContext OnStatsFetched(getAuthors(site, period, limitMode, date), cached = true)
        }
        val payload = restClient.fetchAuthors(site, period, date, limitMode.limit + 1, forced)
        return@withDefaultContext when {
            payload.isError -> OnStatsFetched(payload.error)
            payload.response != null -> {
                sqlUtils.insert(site, payload.response, period, date, limitMode.limit)
                OnStatsFetched(timeStatsMapper.map(payload.response, limitMode))
            }
            else -> OnStatsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getAuthors(site: SiteModel, period: StatsGranularity, limitMode: LimitMode, date: Date) =
            coroutineEngine.run(STATS, this, "getAuthors") {
                sqlUtils.select(site, period, date)?.let { timeStatsMapper.map(it, limitMode) }
            }
}
