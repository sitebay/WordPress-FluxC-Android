package org.sitebay.android.fluxc.network.rest.wpcom.scan

import com.google.gson.annotations.SerializedName
import org.sitebay.android.fluxc.network.Response
import org.sitebay.android.fluxc.network.rest.wpcom.scan.threat.Threat

data class FetchScanHistoryResponse(
    @SerializedName("threats") val threats: List<Threat>?
) : Response
