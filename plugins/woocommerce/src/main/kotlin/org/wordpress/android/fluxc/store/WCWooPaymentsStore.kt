package org.wordpress.android.fluxc.store

import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.payments.woo.WooPaymentsDepositsOverviewEntity
import org.wordpress.android.fluxc.network.rest.wpcom.wc.WooPayload
import org.wordpress.android.fluxc.network.rest.wpcom.wc.payments.woo.WooPaymentsDepositsOverviewApiResponse
import org.wordpress.android.fluxc.network.rest.wpcom.wc.payments.woo.WooPaymentsRestClient
import org.wordpress.android.fluxc.persistence.dao.WooPaymentsDepositsOverviewDao
import org.wordpress.android.fluxc.persistence.mappers.WooPaymentsDepositsOverviewMapper
import org.wordpress.android.fluxc.tools.CoroutineEngine
import org.wordpress.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCWooPaymentsStore @Inject constructor(
    private val coroutineEngine: CoroutineEngine,
    private val restClient: WooPaymentsRestClient,
    private val dao: WooPaymentsDepositsOverviewDao,
    private val mapper: WooPaymentsDepositsOverviewMapper
) {
    suspend fun fetchDepositsOverview(site: SiteModel): WooPayload<WooPaymentsDepositsOverviewApiResponse> =
        coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchDepositsOverview") {
            restClient.fetchDepositsOverview(site)
        }

    suspend fun getDepositsOverviewAll(site: SiteModel): WooPaymentsDepositsOverviewEntity? =
        coroutineEngine.withDefaultContext(AppLog.T.API, this, "getDepositsOverviewAll") {
            dao.get(site.localId())
        }

    suspend fun insertDepositsOverview(depositsOverview: WooPaymentsDepositsOverviewEntity) =
        coroutineEngine.withDefaultContext(AppLog.T.API, this, "insertDepositsOverview") {
            dao.insert(depositsOverview)
        }

    suspend fun deleteDepositsOverview(site: SiteModel) =
        coroutineEngine.withDefaultContext(AppLog.T.API, this, "deleteDepositsOverview") {
            dao.delete(site.localId())
        }
}