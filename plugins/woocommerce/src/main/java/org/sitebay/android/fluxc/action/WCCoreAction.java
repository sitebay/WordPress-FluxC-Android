package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.SiteModel;
import org.sitebay.android.fluxc.store.WooCommerceStore.FetchApiVersionResponsePayload;
import org.sitebay.android.fluxc.store.WooCommerceStore.FetchWCProductSettingsResponsePayload;
import org.sitebay.android.fluxc.store.WooCommerceStore.FetchWCSiteSettingsResponsePayload;

@ActionEnum
public enum WCCoreAction implements IAction {
    // Remote actions
    @Action(payloadType = SiteModel.class)
    FETCH_SITE_API_VERSION,
    @Action(payloadType = SiteModel.class)
    FETCH_SITE_SETTINGS,
    @Action(payloadType = SiteModel.class)
    FETCH_PRODUCT_SETTINGS,

    // Remote responses
    @Action(payloadType = FetchApiVersionResponsePayload.class)
    FETCHED_SITE_API_VERSION,
    @Action(payloadType = FetchWCSiteSettingsResponsePayload.class)
    FETCHED_SITE_SETTINGS,
    @Action(payloadType = FetchWCProductSettingsResponsePayload.class)
    FETCHED_PRODUCT_SETTINGS
}
