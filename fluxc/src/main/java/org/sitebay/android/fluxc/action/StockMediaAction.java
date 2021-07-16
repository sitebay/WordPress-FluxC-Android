package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.store.StockMediaStore;

@ActionEnum
public enum StockMediaAction implements IAction {
    // Remote actions
    @Action(payloadType = StockMediaStore.FetchStockMediaListPayload.class)
    FETCH_STOCK_MEDIA
}
