package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction
import org.sitebay.android.fluxc.store.TransactionsStore.CreateShoppingCartPayload
import org.sitebay.android.fluxc.store.TransactionsStore.RedeemShoppingCartPayload

@ActionEnum
enum class TransactionAction : IAction {
    // Remote actions
    FETCH_SUPPORTED_COUNTRIES,
    @Action(payloadType = CreateShoppingCartPayload::class)
    CREATE_SHOPPING_CART,
    @Action(payloadType = RedeemShoppingCartPayload::class)
    REDEEM_CART_WITH_CREDITS
}
