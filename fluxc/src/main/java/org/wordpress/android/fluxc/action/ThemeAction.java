package org.wordpress.android.fluxc.action;

import org.wordpress.android.fluxc.annotations.Action;
import org.wordpress.android.fluxc.annotations.ActionEnum;
import org.wordpress.android.fluxc.annotations.action.IAction;
import org.wordpress.android.fluxc.model.SiteModel;
import org.wordpress.android.fluxc.model.ThemeModel;
import org.wordpress.android.fluxc.store.ThemeStore.SearchThemesPayload;
import org.wordpress.android.fluxc.store.ThemeStore.SearchedThemesPayload;
import org.wordpress.android.fluxc.store.ThemeStore.FetchedCurrentThemePayload;
import org.wordpress.android.fluxc.store.ThemeStore.FetchedThemesPayload;
import org.wordpress.android.fluxc.store.ThemeStore.ActivateThemePayload;

@ActionEnum
public enum ThemeAction implements IAction {
    // Remote actions
    @Action
    FETCH_WP_COM_THEMES,
    @Action(payloadType = SiteModel.class)
    FETCH_INSTALLED_THEMES, // Jetpack only
    @Action(payloadType = SiteModel.class)
    FETCH_PURCHASED_THEMES,
    @Action(payloadType = SiteModel.class)
    FETCH_CURRENT_THEME,
    @Action(payloadType = SearchThemesPayload.class)
    SEARCH_THEMES,
    @Action(payloadType = ActivateThemePayload.class)
    ACTIVATE_THEME,
    @Action(payloadType = ActivateThemePayload.class)
    INSTALL_THEME,
    @Action(payloadType = ActivateThemePayload.class)
    DELETE_THEME,

    // Remote responses
    @Action(payloadType = FetchedThemesPayload.class)
    FETCHED_WP_COM_THEMES,
    @Action(payloadType = FetchedThemesPayload.class)
    FETCHED_INSTALLED_THEMES,
    @Action(payloadType = FetchedThemesPayload.class)
    FETCHED_PURCHASED_THEMES,
    @Action(payloadType = FetchedCurrentThemePayload.class)
    FETCHED_CURRENT_THEME,
    @Action(payloadType = SearchedThemesPayload.class)
    SEARCHED_THEMES,
    @Action(payloadType = ActivateThemePayload.class)
    ACTIVATED_THEME,
    @Action(payloadType = ActivateThemePayload.class)
    INSTALLED_THEME,
    @Action(payloadType = ActivateThemePayload.class)
    DELETED_THEME,

    // Local actions
    @Action(payloadType = ThemeModel.class)
    REMOVE_THEME,
    @Action(payloadType = SiteModel.class)
    REMOVE_SITE_THEMES
}