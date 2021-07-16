package org.sitebay.android.fluxc.model.shippinglabels

import org.sitebay.android.fluxc.model.shippinglabels.WCShippingLabelModel.ShippingLabelAddress

sealed class WCAddressVerificationResult {
    data class Valid(val suggestedAddress: ShippingLabelAddress) : WCAddressVerificationResult()
    data class InvalidAddress(val message: String) : WCAddressVerificationResult()
    data class InvalidRequest(val message: String) : WCAddressVerificationResult()
}
