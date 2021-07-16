package org.sitebay.android.fluxc.endpoints

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.sitebay.android.fluxc.generated.endpoint.WOOCOMMERCE
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class WCWPAPIEndpointTest {
    @Test
    fun testAllEndpoints() {
        // Orders
        assertEquals("/orders/", WOOCOMMERCE.orders.endpoint)
    }

    @Test
    fun testAllUrls() {
        // Orders
        assertEquals("/wc/v3/orders/", WOOCOMMERCE.orders.pathV3)
    }

    @Test
    fun testRevenueStatsUrl() {
        assertEquals("/wc-analytics/reports/revenue/stats/", WOOCOMMERCE.reports.revenue.stats.pathV4Analytics)
    }
}
