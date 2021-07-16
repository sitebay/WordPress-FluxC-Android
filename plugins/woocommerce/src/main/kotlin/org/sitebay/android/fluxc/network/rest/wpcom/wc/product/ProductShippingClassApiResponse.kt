package org.sitebay.android.fluxc.network.rest.wpcom.wc.product

import org.sitebay.android.fluxc.network.Response

@Suppress("PropertyName")
class ProductShippingClassApiResponse : Response {
    var id: Long = 0L

    var name: String? = null
    var slug: String? = null
    var description: String? = null

    var count = 0
}
