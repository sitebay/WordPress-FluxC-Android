package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.store.ReaderStore.ReaderSearchSitesPayload;
import org.sitebay.android.fluxc.store.ReaderStore.ReaderSearchSitesResponsePayload;

@ActionEnum
public enum ReaderAction implements IAction {
    // Remote actions
    @Action(payloadType = ReaderSearchSitesPayload.class)
    READER_SEARCH_SITES,

    // Remote responses
    @Action(payloadType = ReaderSearchSitesResponsePayload.class)
    READER_SEARCHED_SITES
}
