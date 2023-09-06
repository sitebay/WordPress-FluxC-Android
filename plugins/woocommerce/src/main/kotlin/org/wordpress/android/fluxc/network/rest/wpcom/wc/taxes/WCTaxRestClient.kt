package org.wordpress.android.fluxc.network.rest.wpcom.wc.taxes

import org.wordpress.android.fluxc.generated.endpoint.WOOCOMMERCE
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.network.rest.wpapi.WPAPIResponse
import org.wordpress.android.fluxc.network.rest.wpcom.wc.WooNetwork
import org.wordpress.android.fluxc.network.rest.wpcom.wc.WooPayload
import org.wordpress.android.fluxc.network.rest.wpcom.wc.toWooError
import org.wordpress.android.fluxc.utils.toWooPayload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCTaxRestClient @Inject constructor(private val wooNetwork: WooNetwork) {
    suspend fun fetchTaxClassList(
        site: SiteModel
    ): WooPayload<Array<TaxClassApiResponse>> {
        val url = WOOCOMMERCE.taxes.classes.pathV3

        val response = wooNetwork.executeGetGsonRequest(
            site,
            url,
            Array<TaxClassApiResponse>::class.java
        )
        return response.toWooPayload()
    }

    suspend fun fetchTaxRateList(
        site: SiteModel,
        page: Int,
        pageSize: Int,
        searchQuery: String? = null
    ): WooPayload<Array<TaxRateApiResponse>> {
        val url = WOOCOMMERCE.taxes.pathV3

        val response = wooNetwork.executeGetGsonRequest(
            site,
            url,
            Array<TaxRateApiResponse>::class.java,
            mutableMapOf<String, String>().apply {
                put("page", page.toString())
                put("per_page", pageSize.toString())
                searchQuery?.let {
                    put("search", searchQuery)
                }
            }
        )
        return when (response) {
            is WPAPIResponse.Success -> {
                WooPayload(response.data)
            }
            is WPAPIResponse.Error -> {
                WooPayload(response.error.toWooError())
            }
        }
    }

    data class TaxClassApiResponse(
        val name: String? = null,
        val slug: String? = null
    )

    data class TaxRateApiResponse(
        val id: Int,
        val country: String = "",
        val state: String = "",
        val postcode: String = "",
        val city: String = "",
        val postCodes: List<String>? = null,
        val cities: List<String>? = null,
        val rate: String = "",
        val name: String = "",
        val priority: Int = 0,
        val compound: Boolean = false,
        val shipping: Boolean = false,
        val order: Int = 0,
        val taxClass: String = "",
    )
}
