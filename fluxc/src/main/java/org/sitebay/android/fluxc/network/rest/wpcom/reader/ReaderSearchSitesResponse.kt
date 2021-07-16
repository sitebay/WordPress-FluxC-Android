package org.sitebay.android.fluxc.network.rest.wpcom.reader

import com.google.gson.annotations.JsonAdapter

import org.sitebay.android.fluxc.model.ReaderSiteModel

@JsonAdapter(ReaderSearchSitesDeserializer::class)
class ReaderSearchSitesResponse(
    val sites: List<ReaderSiteModel>
)
