package org.sitebay.android.fluxc.network.rest.wpcom.stats.insights

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.network.BaseRequest.BaseNetworkError
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.NETWORK_ERROR
import org.sitebay.android.fluxc.network.UserAgent
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequest.WPComGsonNetworkError
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Error
import org.sitebay.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Success
import org.sitebay.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.sitebay.android.fluxc.network.rest.wpcom.stats.insights.MostPopularRestClient.MostPopularResponse
import org.sitebay.android.fluxc.store.StatsStore.StatsErrorType.API_ERROR
import org.sitebay.android.fluxc.test

@RunWith(MockitoJUnitRunner::class)
class MostPopularRestClientTest {
    @Mock private lateinit var dispatcher: Dispatcher
    @Mock private lateinit var wpComGsonRequestBuilder: WPComGsonRequestBuilder
    @Mock private lateinit var site: SiteModel
    @Mock private lateinit var requestQueue: RequestQueue
    @Mock private lateinit var accessToken: AccessToken
    @Mock private lateinit var userAgent: UserAgent
    private lateinit var urlCaptor: KArgumentCaptor<String>
    private lateinit var paramsCaptor: KArgumentCaptor<Map<String, String>>
    private lateinit var restClient: MostPopularRestClient
    private val siteId: Long = 12
    @Before
    fun setUp() {
        urlCaptor = argumentCaptor()
        paramsCaptor = argumentCaptor()
        restClient = MostPopularRestClient(
                dispatcher,
                wpComGsonRequestBuilder,
                null,
                requestQueue,
                accessToken,
                userAgent
        )
    }

    @Test
    fun `returns most popular success response`() = test {
        val response = mock<MostPopularResponse>()
        initMostPopularResponse(response)

        val responseModel = restClient.fetchMostPopularInsights(site, false)

        assertThat(responseModel.response).isNotNull
        assertThat(responseModel.response).isEqualTo(response)
        assertThat(urlCaptor.lastValue).isEqualTo("https://mytest.sitebay.org/api/sites/12/stats/insights/")
        assertThat(paramsCaptor.lastValue).isEmpty()
    }

    @Test
    fun `returns most popular error response`() = test {
        val errorMessage = "message"
        initMostPopularResponse(
                error = WPComGsonNetworkError(
                        BaseNetworkError(
                                NETWORK_ERROR,
                                errorMessage,
                                VolleyError(errorMessage)
                        )
                )
        )

        val responseModel = restClient.fetchMostPopularInsights(site, false)

        assertThat(responseModel.error).isNotNull
        assertThat(responseModel.error.type).isEqualTo(API_ERROR)
        assertThat(responseModel.error.message).isEqualTo(errorMessage)
    }

    private suspend fun initMostPopularResponse(
        data: MostPopularResponse? = null,
        error: WPComGsonNetworkError? = null
    ): Response<MostPopularResponse> {
        val response = if (error != null) Error<MostPopularResponse>(error) else Success(data ?: mock())
        whenever(
                wpComGsonRequestBuilder.syncGetRequest(
                        any(),
                        urlCaptor.capture(),
                        paramsCaptor.capture(),
                        eq(MostPopularResponse::class.java),
                        eq(true),
                        any(),
                        eq(false)
                )
        ).thenReturn(response)
        whenever(site.siteId).thenReturn(siteId)
        return response
    }
}
