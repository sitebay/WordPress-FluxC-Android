package org.wordpress.android.fluxc.wc.pay

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.wordpress.android.fluxc.network.rest.wpcom.wc.WooPayload
import org.wordpress.android.fluxc.network.rest.wpcom.wc.pay.ConnectionTokenApiResponse
import org.wordpress.android.fluxc.network.rest.wpcom.wc.pay.PayRestClient
import org.wordpress.android.fluxc.store.WCPayStore
import org.wordpress.android.fluxc.test
import org.wordpress.android.fluxc.tools.initCoroutineEngine

@RunWith(MockitoJUnitRunner::class)
class WCPayStoreTest {
    private val restClient = mock<PayRestClient>()

    private lateinit var store: WCPayStore

    @Before
    fun setUp() {
        store = WCPayStore(initCoroutineEngine(), restClient)
    }

    @Test
    fun `given server returns valid token, when fetchConnectionToken, then result contains the token`() = test {
        val token = "valid token"
        whenever(restClient.fetchConnectionToken(any())).thenReturn(
                WooPayload(ConnectionTokenApiResponse(token, false))
        )

        val result = store.fetchConnectionToken(mock())

        assertThat(result.model?.token).isEqualTo(token)
    }

    @Test
    fun `given server response is testMode=true, when fetchConnectionToken, then testMode=true returned`() = test {
        val isTestMode = true
        whenever(restClient.fetchConnectionToken(any())).thenReturn(
                WooPayload(ConnectionTokenApiResponse("", isTestMode))
        )

        val result = store.fetchConnectionToken(mock())

        assertThat(result.model?.isTestMode).isEqualTo(isTestMode)
    }

    @Test
    fun `given server response is error, when fetchConnectionToken, then WooError returned`() = test {
        whenever(restClient.fetchConnectionToken(any())).thenReturn(
                WooPayload(mock())
        )

        val result = store.fetchConnectionToken(mock())

        assertThat(result.error).isNotNull
    }
}
