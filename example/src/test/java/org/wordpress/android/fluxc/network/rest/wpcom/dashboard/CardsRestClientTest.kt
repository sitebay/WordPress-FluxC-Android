package org.wordpress.android.fluxc.network.rest.wpcom.dashboard

import com.android.volley.RequestQueue
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.wordpress.android.fluxc.Dispatcher
import org.wordpress.android.fluxc.UnitTestUtils
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.network.BaseRequest.BaseNetworkError
import org.wordpress.android.fluxc.network.BaseRequest.GenericErrorType
import org.wordpress.android.fluxc.network.UserAgent
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequest.WPComGsonNetworkError
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response
import org.wordpress.android.fluxc.network.rest.wpcom.WPComGsonRequestBuilder.Response.Success
import org.wordpress.android.fluxc.network.rest.wpcom.auth.AccessToken
import org.wordpress.android.fluxc.network.rest.wpcom.dashboard.CardsRestClient.CardsResponse
import org.wordpress.android.fluxc.network.rest.wpcom.dashboard.CardsRestClient.PostResponse
import org.wordpress.android.fluxc.network.rest.wpcom.dashboard.CardsRestClient.PostsResponse
import org.wordpress.android.fluxc.store.dashboard.CardsStore.CardsErrorType
import org.wordpress.android.fluxc.store.dashboard.CardsStore.FetchedCardsPayload
import org.wordpress.android.fluxc.test
import java.text.SimpleDateFormat

/* DATE */

private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"

/* RESPONSE */

private val DRAFT_POST_RESPONSE_TWO = PostResponse(
        id = 708,
        title = null,
        content = "<!-- wp:paragraph -->\n<p>Draft Content 2</p>\n<!-- /wp:paragraph -->",
        date = SimpleDateFormat(DATE_FORMAT_PATTERN).parse("2021-11-02 17:48:00")!!,
        featuredImage = "https://test.blog/wp-content/uploads/2021/11/draft-photo-2.jpeg?w=200"
)

private val DRAFT_POST_RESPONSE_ONE = PostResponse(
        id = 659,
        title = "Draft Title 1",
        content = "<!-- wp:paragraph -->\n<p>Draft Content 1</p>\n<!-- /wp:paragraph -->",
        date = SimpleDateFormat(DATE_FORMAT_PATTERN).parse("2021-10-27 15:26:01")!!,
        featuredImage = null
)

private val SCHEDULED_POST_RESPONSE_ONE = PostResponse(
        id = 762,
        title = "Scheduled Title 1",
        content = null,
        date = SimpleDateFormat(DATE_FORMAT_PATTERN).parse("2021-11-05 11:05:30")!!,
        featuredImage = "https://test.blog/wp-content/uploads/2021/11/scheduled-photo-1.jpeg?w=200"
)

private val POSTS_RESPONSE = PostsResponse(
        hasPublished = true,
        draft = listOf(
                DRAFT_POST_RESPONSE_TWO,
                DRAFT_POST_RESPONSE_ONE
        ),
        scheduled = listOf(
                SCHEDULED_POST_RESPONSE_ONE
        )
)

private val CARDS_RESPONSE = CardsResponse(
        posts = POSTS_RESPONSE
)

@RunWith(MockitoJUnitRunner::class)
class CardsRestClientTest {
    @Mock private lateinit var wpComGsonRequestBuilder: WPComGsonRequestBuilder
    @Mock private lateinit var dispatcher: Dispatcher
    @Mock private lateinit var requestQueue: RequestQueue
    @Mock private lateinit var accessToken: AccessToken
    @Mock private lateinit var userAgent: UserAgent
    @Mock private lateinit var site: SiteModel

    private lateinit var urlCaptor: KArgumentCaptor<String>
    private lateinit var restClient: CardsRestClient

    private val siteId: Long = 1

    @Before
    fun setUp() {
        urlCaptor = argumentCaptor()
        restClient = CardsRestClient(
                wpComGsonRequestBuilder,
                dispatcher,
                null,
                requestQueue,
                accessToken,
                userAgent
        )
    }

    @Test
    fun `when fetch cards gets triggered, then the correct request url is used`() = test {
        val json = UnitTestUtils.getStringFromResourceFile(javaClass, DASHBOARD_CARDS_JSON)
        initFetchCards(data = getCardsResponseFromJsonString(json))

        restClient.fetchCards(site)

        assertEquals(urlCaptor.firstValue, "$API_SITE_PATH/${site.siteId}/$API_DASHBOARD_CARDS_PATH")
    }

    @Test
    fun `given success call, when fetch cards gets triggered, then cards response is returned`() = test {
        val json = UnitTestUtils.getStringFromResourceFile(javaClass, DASHBOARD_CARDS_JSON)
        initFetchCards(data = getCardsResponseFromJsonString(json))

        val result = restClient.fetchCards(site)

        assertSuccess(CARDS_RESPONSE, result)
    }

    @Test
    fun `given timeout, when fetch cards gets triggered, then return cards timeout error`() = test {
        initFetchCards(error = WPComGsonNetworkError(BaseNetworkError(GenericErrorType.TIMEOUT)))

        val result = restClient.fetchCards(site)

        assertError(CardsErrorType.TIMEOUT, result)
    }

    @Test
    fun `given network error, when fetch cards gets triggered, then return cards api error`() = test {
        initFetchCards(error = WPComGsonNetworkError(BaseNetworkError(GenericErrorType.NETWORK_ERROR)))

        val result = restClient.fetchCards(site)

        assertError(CardsErrorType.API_ERROR, result)
    }

    @Test
    fun `given invalid response, when fetch cards gets triggered, then return cards invalid response error`() = test {
        initFetchCards(error = WPComGsonNetworkError(BaseNetworkError(GenericErrorType.INVALID_RESPONSE)))

        val result = restClient.fetchCards(site)

        assertError(CardsErrorType.INVALID_RESPONSE, result)
    }

    @Test
    fun `given not authenticated, when fetch cards gets triggered, then return cards auth required error`() = test {
        initFetchCards(error = WPComGsonNetworkError(BaseNetworkError(GenericErrorType.NOT_AUTHENTICATED)))

        val result = restClient.fetchCards(site)

        assertError(CardsErrorType.AUTHORIZATION_REQUIRED, result)
    }

    @Test
    fun `given unknown error, when fetch cards gets triggered, then return cards generic error`() = test {
        initFetchCards(error = WPComGsonNetworkError(BaseNetworkError(GenericErrorType.UNKNOWN)))

        val result = restClient.fetchCards(site)

        assertError(CardsErrorType.GENERIC_ERROR, result)
    }

    private fun getCardsResponseFromJsonString(json: String): CardsResponse {
        val responseType = object : TypeToken<CardsResponse>() {}.type
        return GsonBuilder().setDateFormat(DATE_FORMAT_PATTERN)
                .create().fromJson(json, responseType) as CardsResponse
    }

    private suspend fun initFetchCards(
        data: CardsResponse? = null,
        error: WPComGsonNetworkError? = null
    ): Response<CardsResponse> {
        val nonNullData = data ?: mock()
        val response = if (error != null) Response.Error(error) else Success(nonNullData)
        whenever(
                wpComGsonRequestBuilder.syncGetRequest(
                        eq(restClient),
                        urlCaptor.capture(),
                        eq(mapOf()),
                        eq(CardsResponse::class.java),
                        eq(false),
                        any(),
                        eq(false)
                )
        ).thenReturn(response)
        whenever(site.siteId).thenReturn(siteId)
        return response
    }

    @Suppress("SameParameterValue")
    private fun assertSuccess(
        expected: CardsResponse,
        actual: FetchedCardsPayload<CardsResponse>
    ) {
        with(actual) {
            assertEquals(site, this@CardsRestClientTest.site)
            assertFalse(isError)
            assertEquals(FetchedCardsPayload(expected), this)
        }
    }

    private fun assertError(
        expected: CardsErrorType,
        actual: FetchedCardsPayload<CardsResponse>
    ) {
        with(actual) {
            assertEquals(site, this@CardsRestClientTest.site)
            assertTrue(isError)
            assertEquals(expected, error.type)
            assertEquals(null, error.message)
        }
    }

    companion object {
        private const val API_BASE_PATH = "https://public-api.wordpress.com/wpcom/v2"
        private const val API_SITE_PATH = "$API_BASE_PATH/sites"
        private const val API_DASHBOARD_CARDS_PATH = "dashboard/cards/"

        private const val DASHBOARD_CARDS_JSON = "wp/dashboard/cards.json"
    }
}