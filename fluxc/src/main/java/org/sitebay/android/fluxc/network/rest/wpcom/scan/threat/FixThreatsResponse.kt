package org.sitebay.android.fluxc.network.rest.wpcom.scan.threat

import com.google.gson.annotations.SerializedName
import org.sitebay.android.fluxc.network.Response

data class FixThreatsResponse(@SerializedName("ok") val ok: Boolean?) : Response
