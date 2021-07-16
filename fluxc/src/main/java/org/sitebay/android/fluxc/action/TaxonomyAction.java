package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.SiteModel;
import org.sitebay.android.fluxc.model.TermModel;
import org.sitebay.android.fluxc.store.TaxonomyStore.FetchTermResponsePayload;
import org.sitebay.android.fluxc.store.TaxonomyStore.FetchTermsPayload;
import org.sitebay.android.fluxc.store.TaxonomyStore.FetchTermsResponsePayload;
import org.sitebay.android.fluxc.store.TaxonomyStore.RemoteTermPayload;

@ActionEnum
public enum TaxonomyAction implements IAction {
    // Remote actions
    @Action(payloadType = SiteModel.class)
    FETCH_CATEGORIES,
    @Action(payloadType = SiteModel.class)
    FETCH_TAGS,
    @Action(payloadType = FetchTermsPayload.class)
    FETCH_TERMS,
    @Action(payloadType = RemoteTermPayload.class)
    FETCH_TERM,
    @Action(payloadType = RemoteTermPayload.class)
    PUSH_TERM,
    @Action(payloadType = RemoteTermPayload.class)
    DELETE_TERM,

    // Remote responses
    @Action(payloadType = FetchTermsResponsePayload.class)
    FETCHED_TERMS,
    @Action(payloadType = FetchTermResponsePayload.class)
    FETCHED_TERM,
    @Action(payloadType = RemoteTermPayload.class)
    PUSHED_TERM,
    @Action(payloadType = RemoteTermPayload.class)
    DELETED_TERM,

    // Local actions
    @Action(payloadType = TermModel.class)
    UPDATE_TERM,
    @Action(payloadType = TermModel.class)
    REMOVE_TERM,
    @Action
    REMOVE_ALL_TERMS
}

