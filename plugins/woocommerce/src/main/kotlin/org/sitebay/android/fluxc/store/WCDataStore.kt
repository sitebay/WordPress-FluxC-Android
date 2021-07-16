package org.sitebay.android.fluxc.store

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.data.WCCountryMapper
import org.sitebay.android.fluxc.model.data.WCLocationModel
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.UNKNOWN
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooError
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooErrorType.GENERIC_ERROR
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooResult
import org.sitebay.android.fluxc.network.rest.wpcom.wc.data.WCDataRestClient
import org.sitebay.android.fluxc.persistence.WCCountriesSqlUtils
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCDataStore @Inject constructor(
    private val restClient: WCDataRestClient,
    private val coroutineEngine: CoroutineEngine,
    private val mapper: WCCountryMapper
) {
    /**
     * Returns a list of countries
     */
    fun getCountries(): List<WCLocationModel> =
            WCCountriesSqlUtils.getCountries()

    /**
     * Returns a list of states
     */
    fun getStates(country: String): List<WCLocationModel> =
            WCCountriesSqlUtils.getStates(country)

    suspend fun fetchCountriesAndStates(site: SiteModel): WooResult<List<WCLocationModel>> {
        return coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchCountries") {
            val response = restClient.fetchCountries(site)
            return@withDefaultContext when {
                response.isError -> {
                    WooResult(response.error)
                }
                response.result != null -> {
                    val locationModels = response.result.flatMap {
                        mapper.map(it)
                    }

                    // delete existing tax classes for site before adding incoming entries
                    WCCountriesSqlUtils.insertOrUpdateLocations(locationModels)
                    WooResult(locationModels)
                }
                else -> WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }
        }
    }
}
