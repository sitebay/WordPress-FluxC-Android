package org.sitebay.android.fluxc.store

import org.sitebay.android.fluxc.model.SiteModel
import org.sitebay.android.fluxc.model.attribute.WCGlobalAttributeMapper
import org.sitebay.android.fluxc.model.attribute.WCGlobalAttributeModel
import org.sitebay.android.fluxc.network.BaseRequest.GenericErrorType.UNKNOWN
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooError
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooErrorType.GENERIC_ERROR
import org.sitebay.android.fluxc.network.rest.wpcom.wc.WooResult
import org.sitebay.android.fluxc.network.rest.wpcom.wc.product.attributes.ProductAttributeRestClient
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.deleteSingleStoredAttribute
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.fetchSingleStoredAttribute
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.getCurrentAttributes
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.insertAttributeTermsFromScratch
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.insertFromScratchCompleteAttributesList
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.insertOrUpdateSingleAttribute
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.insertSingleAttribute
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.updateSingleAttributeTermsMapping
import org.sitebay.android.fluxc.persistence.WCGlobalAttributeSqlUtils.updateSingleStoredAttribute
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WCGlobalAttributeStore @Inject constructor(
    private val restClient: ProductAttributeRestClient,
    private val mapper: WCGlobalAttributeMapper,
    private val coroutineEngine: CoroutineEngine
) {
    suspend fun fetchStoreAttributes(
        site: SiteModel
    ): WooResult<List<WCGlobalAttributeModel>> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "fetchStoreAttributes") {
                restClient.fetchProductFullAttributesList(site)
                        .asWooResult()
                        .model
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { mapper.responseToAttributeModelList(it, site) }
                        ?.let {
                            insertFromScratchCompleteAttributesList(it, site.id)
                            getCurrentAttributes(site.id)
                        }
                        ?.let { WooResult(it) }
                        ?: getCurrentAttributes(site.id)
                                .takeIf { it.isNotEmpty() }
                                ?.let { WooResult(it) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    fun loadCachedStoreAttributes(
        site: SiteModel
    ) = WooResult(getCurrentAttributes(site.id))

    suspend fun fetchAttributeTerms(
        site: SiteModel,
        attributeID: Long
    ) = restClient.fetchAllAttributeTerms(site, attributeID)
            .result?.map { mapper.responseToAttributeTermModel(it, attributeID.toInt(), site) }
            ?.apply {
                insertAttributeTermsFromScratch(attributeID.toInt(), site.id, this)
                map { it.remoteId.toString() }
                        .takeIf { it.isNotEmpty() }
                        ?.reduce { total, new -> "$total;$new" }
                        ?.let { termsId ->
                            updateSingleAttributeTermsMapping(attributeID.toInt(), termsId, site.id)
                                    ?: handleMissingAttribute(site, attributeID, termsId)
                        }
            }
            ?.let { WooResult(it) }

    suspend fun fetchAttribute(
        site: SiteModel,
        attributeID: Long,
        withTerms: Boolean = false
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "createStoreAttributes") {
                restClient.fetchSingleAttribute(site, attributeID)
                        .asWooResult()
                        .model
                        ?.let { mapper.responseToAttributeModel(it, site) }
                        ?.let { insertOrUpdateSingleAttribute(it, site.id) }
                        ?.apply { takeIf { withTerms }?.let { fetchAttributeTerms(site, attributeID) } }
                        ?.let { fetchSingleStoredAttribute(attributeID.toInt(), site.id) }
                        ?.let { WooResult(it) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    suspend fun createAttribute(
        site: SiteModel,
        name: String,
        slug: String = name,
        type: String = "select",
        orderBy: String = "menu_order",
        hasArchives: Boolean = false
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "createStoreAttributes") {
                restClient.postNewAttribute(
                        site, mapOf(
                        "name" to name,
                        "slug" to slug,
                        "type" to type,
                        "order_by" to orderBy,
                        "has_archives" to hasArchives.toString()
                )
                )
                        .asWooResult()
                        .model
                        ?.let { mapper.responseToAttributeModel(it, site) }
                        ?.let { insertSingleAttribute(it) }
                        ?.let { WooResult(it) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    suspend fun updateAttribute(
        site: SiteModel,
        attributeID: Long,
        name: String,
        slug: String = name,
        type: String = "select",
        orderBy: String = "menu_order",
        hasArchives: Boolean = false
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "updateStoreAttributes") {
                restClient.updateExistingAttribute(
                        site, attributeID, mapOf(
                        "id" to attributeID.toString(),
                        "name" to name,
                        "slug" to slug,
                        "type" to type,
                        "order_by" to orderBy,
                        "has_archives" to hasArchives.toString()
                )
                )
                        .asWooResult()
                        .model
                        ?.let { mapper.responseToAttributeModel(it, site) }
                        ?.let { updateSingleStoredAttribute(it, site.id) }
                        ?.let { WooResult(it) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    suspend fun deleteAttribute(
        site: SiteModel,
        attributeID: Long
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "deleteStoreAttributes") {
                restClient.deleteExistingAttribute(site, attributeID)
                        .asWooResult()
                        .model
                        ?.let { mapper.responseToAttributeModel(it, site) }
                        ?.let { deleteSingleStoredAttribute(it, site.id) }
                        ?.let { WooResult(it) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    suspend fun createOptionValueForAttribute(
        site: SiteModel,
        attributeID: Long,
        term: String
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "createAttributeTerm") {
                restClient.postNewTerm(
                        site, attributeID,
                        mapOf("name" to term)
                )
                        .asWooResult()
                        .model
                        ?.let { fetchAttribute(site, attributeID) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    suspend fun deleteOptionValueFromAttribute(
        site: SiteModel,
        attributeID: Long,
        termID: Long
    ): WooResult<WCGlobalAttributeModel> =
            coroutineEngine.withDefaultContext(AppLog.T.API, this, "deleteAttributeTerm") {
                restClient.deleteExistingTerm(site, attributeID, termID)
                        .asWooResult()
                        .model
                        ?.let { fetchAttribute(site, attributeID) }
                        ?: WooResult(WooError(GENERIC_ERROR, UNKNOWN))
            }

    private suspend fun handleMissingAttribute(
        site: SiteModel,
        attributeID: Long,
        termsId: String
    ) {
        fetchAttribute(site, attributeID)
                .model
                ?.let { updateSingleAttributeTermsMapping(attributeID.toInt(), termsId, site.id) }
    }
}
