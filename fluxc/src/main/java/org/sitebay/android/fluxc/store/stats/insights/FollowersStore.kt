package org.sitebay.android.fluxc.store.stats.insights

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.stats.FollowersModel
import org.sitebay.android.fluxc.model.stats.InsightsMapper
import org.sitebay.android.fluxc.model.stats.LimitMode
import org.sitebay.android.fluxc.model.stats.LimitMode.All
import org.sitebay.android.fluxc.model.stats.PagedMode
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.FollowersRestClient
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.FollowersRestClient.FollowerType
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.FollowersRestClient.FollowerType.EMAIL
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.FollowersRestClient.FollowerType.WP_COM
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.FollowersRestClient.FollowersResponse
import org.sitebay.android.fluxc.persistence.InsightsSqlUtils
import org.sitebay.android.fluxc.persistence.InsightsSqlUtils.EmailFollowersSqlUtils
import org.sitebay.android.fluxc.persistence.InsightsSqlUtils.WpComFollowersSqlUtils
import org.sitebay.android.fluxc.store.StatsStore.OnStatsFetched
import org.sitebay.android.fluxc.store.StatsStore.StatsError
import org.sitebay.android.fluxc.store.StatsStore.StatsErrorType.INVALID_RESPONSE
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog.T.STATS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowersStore
@Inject constructor(
    private val restClient: FollowersRestClient,
    private val wpComFollowersSqlUtils: WpComFollowersSqlUtils,
    private val emailFollowersSqlUtils: EmailFollowersSqlUtils,
    private val insightsMapper: InsightsMapper,
    private val coroutineEngine: CoroutineEngine
) {
    suspend fun fetchWpComFollowers(
        siteModel: SiteModel,
        fetchMode: PagedMode,
        forced: Boolean = false
    ): OnStatsFetched<FollowersModel> {
        return fetchFollowers(siteModel, forced, WP_COM, fetchMode, wpComFollowersSqlUtils)
    }

    suspend fun fetchEmailFollowers(
        siteModel: SiteModel,
        fetchMode: PagedMode,
        forced: Boolean = false
    ): OnStatsFetched<FollowersModel> {
        return fetchFollowers(siteModel, forced, EMAIL, fetchMode, emailFollowersSqlUtils)
    }

    private suspend fun fetchFollowers(
        siteModel: SiteModel,
        forced: Boolean = false,
        followerType: FollowerType,
        fetchMode: PagedMode,
        sqlUtils: InsightsSqlUtils<FollowersResponse>
    ) = coroutineEngine.withDefaultContext(STATS, this, "fetchFollowers") {
        if (!forced && !fetchMode.loadMore && sqlUtils.hasFreshRequest(
                        siteModel,
                        fetchMode.pageSize
                )) {
            return@withDefaultContext OnStatsFetched(
                    getFollowers(
                            siteModel,
                            followerType,
                            cacheMode = LimitMode.Top(fetchMode.pageSize),
                            sqlUtils = sqlUtils
                    ),
                    cached = true
            )
        }
        val nextPage = if (fetchMode.loadMore) {
            val savedFollowers = sqlUtils.selectAll(siteModel).sumBy { it.subscribers.size }
            savedFollowers / fetchMode.pageSize + 1
        } else {
            1
        }

        val responsePayload = restClient.fetchFollowers(siteModel, followerType, nextPage, fetchMode.pageSize, forced)
        return@withDefaultContext when {
            responsePayload.isError -> {
                OnStatsFetched(responsePayload.error)
            }
            responsePayload.response != null -> {
                val replace = !fetchMode.loadMore
                sqlUtils.insert(
                        siteModel,
                        responsePayload.response,
                        replaceExistingData = replace,
                        requestedItems = fetchMode.pageSize
                )
                val followerResponses = sqlUtils.selectAll(siteModel)
                val allFollowers = insightsMapper.mapAndMergeFollowersModels(
                        followerResponses,
                        followerType,
                        All
                )
                OnStatsFetched(allFollowers)
            }
            else -> OnStatsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getWpComFollowers(site: SiteModel, cacheMode: LimitMode): FollowersModel? {
        return getFollowers(site, WP_COM, cacheMode, wpComFollowersSqlUtils)
    }

    fun getEmailFollowers(site: SiteModel, cacheMode: LimitMode): FollowersModel? {
        return getFollowers(site, EMAIL, cacheMode, emailFollowersSqlUtils)
    }

    private fun getFollowers(
        site: SiteModel,
        followerType: FollowerType,
        cacheMode: LimitMode,
        sqlUtils: InsightsSqlUtils<FollowersResponse>
    ) = coroutineEngine.run(STATS, this, "getFollowers") {
        val followerResponses = sqlUtils.selectAll(site)
        insightsMapper.mapAndMergeFollowersModels(followerResponses, followerType, cacheMode)
    }
}
