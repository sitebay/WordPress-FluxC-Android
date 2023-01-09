package org.wordpress.android.fluxc.release

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.wordpress.android.fluxc.example.test.BuildConfig
import org.wordpress.android.fluxc.model.payments.inperson.WCPaymentAccountResult.WCPaymentAccountStatus
import org.wordpress.android.fluxc.store.AccountStore.AuthenticatePayload
import org.wordpress.android.fluxc.store.WCInPersonPaymentsStore
import org.wordpress.android.fluxc.store.WCInPersonPaymentsStore.InPersonPaymentsPluginType.WOOCOMMERCE_PAYMENTS
import org.wordpress.android.fluxc.store.WCOrderStore
import javax.inject.Inject

class ReleaseStack_InPersonPaymentsWCPayTest : ReleaseStack_WCBase() {
    @Inject internal lateinit var store: WCInPersonPaymentsStore
    @Inject internal lateinit var orderStore: WCOrderStore

    override val testSite: TestSite = TestSite.Specified(siteId = BuildConfig.TEST_WPCOM_SITE_ID_WOO_JP_WCPAY.toLong())

    override fun buildAuthenticatePayload() = AuthenticatePayload(
            BuildConfig.TEST_WPCOM_USERNAME_WOO_JP,
            BuildConfig.TEST_WPCOM_PASSWORD_WOO_JP
    )

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        mReleaseStackAppComponent.inject(this)
        // Register
        init()
    }

    @Test
    fun givenSiteHasWCPayWhenFetchConnectionTokenInvokedThenTokenReturned() = runBlocking {
        val result = store.fetchConnectionToken(WOOCOMMERCE_PAYMENTS, sSite)

        assertTrue(result.model?.token?.isNotEmpty() == true)
    }

    @Test
    fun givenSiteHasWCPayWhenLoadAccountThenTestAccountReturned() = runBlocking {
        val result = store.loadAccount(WOOCOMMERCE_PAYMENTS, sSite)

        assertEquals("US", result.model?.country)
        assertEquals(false, result.model?.hasPendingRequirements)
        assertEquals(false, result.model?.hasOverdueRequirements)
        assertEquals("DO.WPMT.CO", result.model?.statementDescriptor)
        assertEquals("US", result.model?.country)
        assertEquals("usd", result.model?.storeCurrencies?.default)
        assertEquals(listOf("usd"), result.model?.storeCurrencies?.supportedCurrencies)
        assertEquals(WCPaymentAccountStatus.COMPLETE, result.model?.status)
    }

    @Test
    fun givenSiteHasWCPayAndStripeAddressThenLocationDataReturned() = runBlocking {
        val result = store.getStoreLocationForSite(WOOCOMMERCE_PAYMENTS, sSite)

        assertFalse(result.isError)
        assertEquals("tml_EUZ4bQQTxLWMq2", result.locationId)
        assertEquals("Woo WCPay", result.displayName)
        assertEquals("San Francisco", result.address?.city)
        assertEquals("US", result.address?.country)
        assertEquals("1230 Lawton St", result.address?.line1)
        assertEquals("71", result.address?.line2)
        assertEquals("94122", result.address?.postalCode)
        assertEquals("CA", result.address?.state)
    }

    @Test
    fun givenSiteHasWCPayFetchingChargeByIdThenChargeReturned() = runBlocking {
        val chargeId = "ch_3KOLxm2HswaZkMX319bDtsay"
        val result = store.fetchPaymentCharge(WOOCOMMERCE_PAYMENTS, sSite, chargeId)

        assertFalse(result.isError)
        assertEquals("9969", result.asWooResult().model?.paymentMethodDetails?.cardDetails?.last4)
        assertEquals(4500, result.asWooResult().model?.amount)
    }

    @Test
    fun givenSiteHasWCPayWhenTransactionSummaryInvokedSummaryIsReturned() = runBlocking {
        val result = store.fetchTransactionsSummary(WOOCOMMERCE_PAYMENTS, sSite)

        assertFalse(result.isError)

        val data = result.result
        assertNotNull(data)
        assertNotNull(data!!.transactionsCount)
        assertNotNull(data.currency)
        assertNotNull(data.net)
        assertNotNull(data.fees)
        assertNotNull(data.total)
    }

    @Test
    fun givenSiteHasWCPayWhenTransactionSummaryInvokedWithParameterSummaryIsReturned() =
        runBlocking {
            val dateAfter = "2021-07-01"

            val result = store.fetchTransactionsSummary(WOOCOMMERCE_PAYMENTS, sSite, dateAfter)

            assertFalse(result.isError)

            val data = result.result
            assertNotNull(data)
            assertNotNull(data!!.transactionsCount)
            assertNotNull(data.currency)
            assertNotNull(data.net)
            assertNotNull(data.fees)
            assertNotNull(data.total)
        }
}
