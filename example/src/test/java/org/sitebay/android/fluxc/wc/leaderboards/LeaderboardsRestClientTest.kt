package org.sitebay.android.fluxc.wc.leaderboards

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.sitebay.android.fluxc.generated.endpoint.WOOCOMMERCE
import org.sitebay.android.fluxc.network.BaseRequest.BaseNetworkError
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.NETWORK_ERROR
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequest.WPComGsonNetworkError
import org.sitebay.android.fluxc.network.rest.wpcom.jetpacktunnel.JetpackTunnelGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.jetpacktunnel.JetpackTunnelGsonRequestBuilder.JetpackResponse.JetpackError
import org.sitebay.android.fluxc.network.rest.wpcom.jetpacktunnel.JetpackTunnelGsonRequestBuilder.JetpackResponse.JetpackSuccess
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooError
import org.sitebay.android.fluxc.network.rest.wpcom.wc.leaderboards.LeaderboardsApiResponse
import org.sitebay.android.fluxc.network.rest.wpcom.wc.leaderboards.LeaderboardsRestClient
import org.sitebay.android.fluxc.store.WCStatsStore.StatsGranularity.DAYS
import org.sitebay.android.fluxc.test
import org.sitebay.android.fluxc.wc.leaderboards.WCLeaderboardsTestFixtures.generateSampleLeaderboardsApiResponse
import org.sitebay.android.fluxc.wc.leaderboards.WCLeaderboardsTestFixtures.stubSite

class LeaderboardsRestClientTest {
    private lateinit var restClientUnderTest: LeaderboardsRestClient
    private lateinit var requestBuilder: JetpackTunnelGsonRequestBuilder
    private lateinit var jetpackSuccessResponse: JetpackSuccess<Array<LeaderboardsApiResponse>>
    private lateinit var jetpackErrorResponse: JetpackError<Array<LeaderboardsApiResponse>>

    @Before
    fun setUp() {
        requestBuilder = mock()
        jetpackSuccessResponse = mock()
        jetpackErrorResponse = mock()
        restClientUnderTest = LeaderboardsRestClient(
                mock(),
                mock(),
                mock(),
                mock(),
                mock(),
                requestBuilder
        )
    }

    @Test
    fun `fetch leaderboards should call syncGetRequest with correct parameters and return expected response`() = test {
        val expectedResult = generateSampleLeaderboardsApiResponse()
        configureSuccessRequest(expectedResult!!)

        val response = restClientUnderTest.fetchLeaderboards(stubSite, DAYS, 1L..22L, 5)
        verify(requestBuilder, times(1)).syncGetRequest(
                restClientUnderTest,
                stubSite,
                WOOCOMMERCE.leaderboards.pathV4Analytics,
                mapOf(
                        "before" to "22",
                        "after" to "1",
                        "per_page" to "5",
                        "interval" to "day"
                ),
                Array<LeaderboardsApiResponse>::class.java
        )
        assertThat(response).isNotNull
        assertThat(response.result).isNotNull
        assertThat(response.error).isNull()
        assertThat(response.result).isEqualTo(expectedResult)
    }

    @Test
    fun `fetch leaderboards should correctly return failure as WooError`() = test {
        configureErrorRequest()
        val response = restClientUnderTest.fetchLeaderboards(
                stubSite,
                DAYS,
                1L..22L,
                5
        )

        assertThat(response).isNotNull
        assertThat(response.result).isNull()
        assertThat(response.error).isNotNull
        assertThat(response.error).isExactlyInstanceOf(WooError::class.java)
    }

    private suspend fun configureSuccessRequest(expectedResult: Array<LeaderboardsApiResponse>) {
        whenever(jetpackSuccessResponse.data).thenReturn(expectedResult)
        whenever(
                requestBuilder.syncGetRequest(
                        restClientUnderTest,
                        stubSite,
                        WOOCOMMERCE.leaderboards.pathV4Analytics,
                        mapOf(
                                "after" to "1",
                                "before" to "22",
                                "per_page" to "5",
                                "interval" to "day"
                        ),
                        Array<LeaderboardsApiResponse>::class.java
                )
        ).thenReturn(jetpackSuccessResponse)
    }

    private suspend fun configureErrorRequest() {
        whenever(jetpackErrorResponse.error).thenReturn(WPComGsonNetworkError(BaseNetworkError(NETWORK_ERROR)))
        whenever(
                requestBuilder.syncGetRequest(
                        restClientUnderTest,
                        stubSite,
                        WOOCOMMERCE.leaderboards.pathV4Analytics,
                        mapOf(
                                "after" to "1",
                                "before" to "22",
                                "per_page" to "5",
                                "interval" to "day"
                        ),
                        Array<LeaderboardsApiResponse>::class.java
                )
        ).thenReturn(jetpackErrorResponse)
    }
}
