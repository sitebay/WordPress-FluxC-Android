package org.wordpress.android.fluxc.store

import kotlinx.coroutines.experimental.withContext
import org.wordpress.android.fluxc.Payload
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.stats.CommentsModel
import org.wordpress.android.fluxc.model.stats.FollowersModel
import org.wordpress.android.fluxc.model.stats.InsightsAllTimeModel
import org.wordpress.android.fluxc.model.stats.InsightsLatestPostModel
import org.wordpress.android.fluxc.model.stats.InsightsMapper
import org.wordpress.android.fluxc.model.stats.InsightsMostPopularModel
import org.wordpress.android.fluxc.model.stats.VisitsModel
import org.wordpress.android.fluxc.network.rest.wpcom.stats.InsightsRestClient
import org.wordpress.android.fluxc.network.rest.wpcom.stats.InsightsRestClient.FollowerType
import org.wordpress.android.fluxc.network.rest.wpcom.stats.InsightsRestClient.FollowerType.EMAIL
import org.wordpress.android.fluxc.network.rest.wpcom.stats.InsightsRestClient.FollowerType.WP_COM
import org.wordpress.android.fluxc.network.utils.StatsGranularity.DAYS
import org.wordpress.android.fluxc.persistence.InsightsSqlUtils
import org.wordpress.android.fluxc.store.InsightsStore.StatsErrorType.INVALID_RESPONSE
import org.wordpress.android.fluxc.utils.CurrentTimeProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.experimental.CoroutineContext

@Singleton
class InsightsStore
@Inject constructor(
    private val restClient: InsightsRestClient,
    private val sqlUtils: InsightsSqlUtils,
    private val insightsMapper: InsightsMapper,
    private val timeProvider: CurrentTimeProvider,
    private val coroutineContext: CoroutineContext
) {
    // All time insights
    suspend fun fetchAllTimeInsights(site: SiteModel, forced: Boolean = false) = withContext(coroutineContext) {
        val payload = restClient.fetchAllTimeInsights(site, forced)
        return@withContext when {
            payload.isError -> OnInsightsFetched(payload.error)
            payload.response != null -> {
                sqlUtils.insert(site, payload.response)
                OnInsightsFetched(insightsMapper.map(payload.response, site))
            }
            else -> OnInsightsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getAllTimeInsights(site: SiteModel): InsightsAllTimeModel? {
        return sqlUtils.selectAllTimeInsights(site)?.let { insightsMapper.map(it, site) }
    }

    // Most popular insights
    suspend fun fetchMostPopularInsights(site: SiteModel, forced: Boolean = false) = withContext(coroutineContext) {
        val payload = restClient.fetchMostPopularInsights(site, forced)
        return@withContext when {
            payload.isError -> OnInsightsFetched(payload.error)
            payload.response != null -> {
                val data = payload.response
                sqlUtils.insert(site, data)
                OnInsightsFetched(
                        insightsMapper.map(data, site)
                )
            }
            else -> OnInsightsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getMostPopularInsights(site: SiteModel): InsightsMostPopularModel? {
        return sqlUtils.selectMostPopularInsights(site)?.let { insightsMapper.map(it, site) }
    }

    // Latest post insights
    suspend fun fetchLatestPostInsights(site: SiteModel, forced: Boolean = false) = withContext(coroutineContext) {
        val latestPost = restClient.fetchLatestPostForInsights(site, forced)
        val postsFound = latestPost.response?.postsFound

        val posts = latestPost.response?.posts
        return@withContext if (postsFound != null && postsFound > 0 && posts != null && posts.isNotEmpty()) {
            val latestPost = posts[0]
            val postStats = restClient.fetchPostStats(site, latestPost.id, forced)
            when {
                postStats.response != null -> {
                    sqlUtils.insert(site, latestPost)
                    sqlUtils.insert(site, postStats.response)
                    OnInsightsFetched(insightsMapper.map(latestPost, postStats.response, site))
                }
                postStats.isError -> OnInsightsFetched(postStats.error)
                else -> OnInsightsFetched()
            }
        } else if (latestPost.isError) {
            OnInsightsFetched(latestPost.error)
        } else {
            OnInsightsFetched()
        }
    }

    fun getLatestPostInsights(site: SiteModel): InsightsLatestPostModel? {
        val latestPostDetailResponse = sqlUtils.selectLatestPostDetail(site)
        val latestPostViewsResponse = sqlUtils.selectLatestPostStats(site)
        return if (latestPostDetailResponse != null && latestPostViewsResponse != null) {
            insightsMapper.map(latestPostDetailResponse, latestPostViewsResponse, site)
        } else {
            null
        }
    }

    // Time period stats
    suspend fun fetchTodayInsights(siteModel: SiteModel, forced: Boolean = false) = withContext(coroutineContext) {
        val response = restClient.fetchTimePeriodStats(siteModel, DAYS, timeProvider.currentDate, forced)
        return@withContext when {
            response.isError -> {
                OnInsightsFetched(response.error)
            }
            response.response != null -> {
                sqlUtils.insert(siteModel, response.response)
                OnInsightsFetched(insightsMapper.map(response.response))
            }
            else -> OnInsightsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getTodayInsights(site: SiteModel): VisitsModel? {
        return sqlUtils.selectTodayInsights(site)?.let { insightsMapper.map(it) }
    }

    // Followers stats
    suspend fun fetchWpComFollowers(siteModel: SiteModel, forced: Boolean = false): OnInsightsFetched<FollowersModel> {
        return fetchFollowers(siteModel, forced, WP_COM)
    }

    suspend fun fetchEmailFollowers(siteModel: SiteModel, forced: Boolean = false): OnInsightsFetched<FollowersModel> {
        return fetchFollowers(siteModel, forced, EMAIL)
    }

    private suspend fun fetchFollowers(
        siteModel: SiteModel,
        forced: Boolean = false,
        followerType: FollowerType
    ) = withContext(coroutineContext) {
        val response = restClient.fetchFollowers(siteModel, followerType, forced = forced)
        return@withContext when {
            response.isError -> {
                OnInsightsFetched(response.error)
            }
            response.response != null -> {
                sqlUtils.insert(siteModel, response.response, followerType)
                OnInsightsFetched(insightsMapper.map(response.response, followerType))
            }
            else -> OnInsightsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getWpComFollowers(site: SiteModel): FollowersModel? {
        return getFollowers(site, WP_COM)
    }

    fun getEmailFollowers(site: SiteModel): FollowersModel? {
        return getFollowers(site, EMAIL)
    }

    private fun getFollowers(
        site: SiteModel,
        followerType: FollowerType
    ): FollowersModel? {
        val wpComResponse = sqlUtils.selectFollowers(site, followerType)
        return wpComResponse?.let { insightsMapper.map(wpComResponse, followerType) }
    }

    // Comments stats
    suspend fun fetchComments(siteModel: SiteModel, forced: Boolean = false) = withContext(coroutineContext) {
        val response = restClient.fetchTopComments(siteModel, forced = forced)
        return@withContext when {
            response.isError -> {
                OnInsightsFetched(response.error)
            }
            response.response != null -> {
                sqlUtils.insert(siteModel, response.response)
                OnInsightsFetched(insightsMapper.map(response.response))
            }
            else -> OnInsightsFetched(StatsError(INVALID_RESPONSE))
        }
    }

    fun getComments(site: SiteModel): CommentsModel? {
        return sqlUtils.selectCommentInsights(site)?.let { insightsMapper.map(it) }
    }

    data class OnInsightsFetched<T>(val model: T? = null) : Store.OnChanged<StatsError>() {
        constructor(error: StatsError) : this() {
            this.error = error
        }
    }

    data class FetchInsightsPayload<T>(
        val response: T? = null
    ) : Payload<StatsError>() {
        constructor(error: StatsError) : this() {
            this.error = error
        }
    }

    enum class StatsErrorType {
        GENERIC_ERROR,
        TIMEOUT,
        API_ERROR,
        AUTHORIZATION_REQUIRED,
        INVALID_RESPONSE
    }

    class StatsError(var type: StatsErrorType, var message: String? = null) : Store.OnChangedError
}