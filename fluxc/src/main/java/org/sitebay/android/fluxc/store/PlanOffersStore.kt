package org.sitebay.android.fluxc.store

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sitebay.android.fluxc.Dispatcher
import org.sitebay.android.fluxc.Payload
import org.sitebay.android.fluxc.action.PlanOffersAction
import org.sitebay.android.fluxc.action.PlanOffersAction.FETCH_PLAN_OFFERS
import org.sitebay.android.fluxc.annotations.action.Action
import org.sitebay.android.fluxc.model.plans.PlanOffersModel
import org.sitebay.android.fluxc.network.BaseRequest
import org.sitebay.android.fluxc.network.rest.wpcom.planoffers.PlanOffersRestClient
import org.sitebay.android.fluxc.persistence.PlanOffersSqlUtils
import org.sitebay.android.fluxc.store.PlanOffersStore.PlanOffersErrorType.GENERIC_ERROR
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanOffersStore @Inject constructor(
    private val planOffersRestClient: PlanOffersRestClient,
    private val planOffersSqlUtils: PlanOffersSqlUtils,
    private val coroutineEngine: CoroutineEngine,
    dispatcher: Dispatcher
) : Store(dispatcher) {
    @Subscribe(threadMode = ThreadMode.ASYNC)
    override fun onAction(action: Action<*>) {
        val actionType = action.type as? PlanOffersAction ?: return
        when (actionType) {
            FETCH_PLAN_OFFERS -> {
                coroutineEngine.launch(AppLog.T.PLANS, this, "FETCH_PLAN_OFFERS") {
                    emitChange(fetchPlanOffers())
                }
            }
        }
    }

    private suspend fun fetchPlanOffers(): OnPlanOffersFetched {
        val fetchedPlanOffersPayload = planOffersRestClient.fetchPlanOffers()

        return if (!fetchedPlanOffersPayload.isError) {
            planOffersSqlUtils.storePlanOffers(fetchedPlanOffersPayload.planOffers!!)
            OnPlanOffersFetched(fetchedPlanOffersPayload.planOffers)
        } else {
            OnPlanOffersFetched(
                    getCachedPlanOffers(),
                    PlansFetchError(GENERIC_ERROR, fetchedPlanOffersPayload.error.message)
            )
        }
    }

    fun getCachedPlanOffers(): List<PlanOffersModel> {
        return planOffersSqlUtils.getPlanOffers()
    }

    override fun onRegister() {
        AppLog.d(AppLog.T.API, PlanOffersStore::class.java.simpleName + " onRegister")
    }

    class PlanOffersFetchedPayload(
        val planOffers: List<PlanOffersModel>? = null
    ) : Payload<BaseRequest.BaseNetworkError>()

    data class OnPlanOffersFetched(
        val planOffers: List<PlanOffersModel>? = null,
        val fetchError: PlansFetchError? = null
    ) : Store.OnChanged<PlansFetchError>() {
        init {
            // we allow setting error from constructor, so it will be a part of data class
            // and used during comparison, so we can test error events
            this.error = fetchError
        }
    }

    data class PlansFetchError(
        val type: PlanOffersErrorType,
        val message: String = ""
    ) : OnChangedError

    enum class PlanOffersErrorType {
        GENERIC_ERROR
    }
}
