package org.sitebay.android.fluxc.store

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.gateways.GatewayMapper
import org.sitebay.android.fluxc.model.gateways.WCGatewayModel
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.UNKNOWN
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooError
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooErrorType.GENERIC_ERROR
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooResult
import org.sitebay.android.fluxc.network.rest.wpcom.wc.gateways.GatewayRestClient
import org.sitebay.android.fluxc.persistence.WCGatewaySqlUtils
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCGatewayStore @Inject constructor(
    private val restClient: GatewayRestClient,
    private val coroutineEngine: CoroutineEngine,
    private val mapper: GatewayMapper
) {
    fun getGateway(site: SiteModel, gatewayId: String): WCGatewayModel? =
            WCGatewaySqlUtils.selectGateway(site, gatewayId)?.let { mapper.map(it) }

    suspend fun fetchGateway(site: SiteModel, gatewayId: String): WooResult<WCGatewayModel> {
        return coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchGateway") {
            val response = restClient.fetchGateway(site, gatewayId)
            return@withDefaultContext when {
                response.isError -> WooResult(response.error)
                response.result != null -> {
                    WCGatewaySqlUtils.insertOrUpdate(site, response.result)
                    WooResult(mapper.map(response.result))
                }
                else -> WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }
        }
    }

    fun getAllGateways(site: SiteModel): List<WCGatewayModel> =
            WCGatewaySqlUtils.selectAllGateways(site).map { mapper.map(it) }

    suspend fun fetchAllGateways(site: SiteModel): WooResult<List<WCGatewayModel>> {
        return coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchAllGateways") {
            val response = restClient.fetchAllGateways(site)
            return@withDefaultContext when {
                response.isError -> {
                    WooResult(response.error)
                }
                response.result != null -> {
                    WCGatewaySqlUtils.insertOrUpdate(site, response.result.toList())
                    WooResult(response.result.map { mapper.map(it) })
                }
                else -> WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }
        }
    }
}
