package org.sitebay.android.fluxc.store

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.taxes.WCTaxClassMapper
import org.sitebay.android.fluxc.model.taxes.WCTaxClassModel
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.UNKNOWN
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooError
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooErrorType.GENERIC_ERROR
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooResult
import org.sitebay.android.fluxc.network.rest.wpcom.wc.taxes.WCTaxRestClient
import org.sitebay.android.fluxc.persistence.WCTaxSqlUtils
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCTaxStore @Inject constructor(
    private val restClient: WCTaxRestClient,
    private val coroutineEngine: CoroutineEngine,
    private val mapper: WCTaxClassMapper
) {
    /**
     * returns a list of tax classes for a specific site in the database
     */
    fun getTaxClassListForSite(site: SiteModel): List<WCTaxClassModel> =
            WCTaxSqlUtils.getTaxClassesForSite(site.id)

    suspend fun fetchTaxClassList(site: SiteModel): WooResult<List<WCTaxClassModel>> {
        return coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchTaxClassList") {
            val response = restClient.fetchTaxClassList(site)
            return@withDefaultContext when {
                response.isError -> {
                    WooResult(response.error)
                }
                response.result != null -> {
                    val taxClassModels = response.result.map {
                        mapper.map(it).apply { localSiteId = site.id }
                    }

                    // delete existing tax classes for site before adding incoming entries
                    WCTaxSqlUtils.deleteTaxClassesForSite(site)
                    WCTaxSqlUtils.insertOrUpdateTaxClasses(taxClassModels)
                    WooResult(taxClassModels)
                }
                else -> WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }
        }
    }
}
