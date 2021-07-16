package org.sitebay.android.fluxc.store.stats.time

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.stats.LimitMode
import org.sitebay.android.fluxc.model.stats.time.TimeStatsMapper
import org.sitebay.android.fluxc.network.rest.wpcom.stats.time.CountryViewsRestClient
import org.sitebay.android.fluxc.network.utils.StatsGranularity
import org.sitebay.android.fluxc.persistence.TimeStatsSqlUtils.CountryViewsSqlUtils
import org.sitebay.android.fluxc.store.StatsStore.OnStatsFetched
import org.sitebay.android.fluxc.store.StatsStore.StatsError
import org.sitebay.android.fluxc.store.StatsStore.StatsErrorType.INVALID_RESPONSE
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog.T.STATS
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryViewsStore
@Inject constructor(
    private val restClient: CountryViewsRestClient,
    private val sqlUtils: CountryViewsSqlUtils,
    private val timeStatsMapper: TimeStatsMapper,
    private val coroutineEngine: CoroutineEngine
) {
    suspend fun fetchCountryViews(
        site: SiteModel,
        granularity: StatsGranularity,
        limitMode: LimitMode.Top,
        date: Date,
        forced: Boolean = false
    ) = coroutineEngine.withDefaultContext(STATS, this, "fetchCountryViews") {
        if (!forced && sqlUtils.hasFreshRequest(site, granularity, date, limitMode.limit)) {
            return@withDefaultContext OnStatsFetched(getCountryViews(site, granularity, limitMode, date), cached = true)
        }
        val payload = restClient.fetchCountryViews(site, granularity, date, limitMode.limit + 1, forced)
        return@withDefaultContext when {
            payload.isError -> OnStatsFetched(payload.error)
            payload.response != null -> {
                sqlUtils.insert(site, payload.response, granularity, date, limitMode.limit)
                OnStatsFetched(timeStatsMapper.map(payload.response, limitMode))
            }
            else -> OnStatsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getCountryViews(
        site: SiteModel,
        period: StatsGranularity,
        limitMode: LimitMode,
        date: Date
    ) = coroutineEngine.run(STATS, this, "getCountryViews") {
        sqlUtils.select(site, period, date)?.let { timeStatsMapper.map(it, limitMode) }
    }
}
