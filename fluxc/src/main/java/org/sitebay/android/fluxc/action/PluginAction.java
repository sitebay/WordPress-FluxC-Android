package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.SiteModel;
import org.sitebay.android.fluxc.store.PluginStore.ConfigureSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.ConfiguredSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.DeleteSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.DeletedSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.FetchPluginDirectoryPayload;
import org.sitebay.android.fluxc.store.PluginStore.FetchedPluginDirectoryPayload;
import org.sitebay.android.fluxc.store.PluginStore.FetchedWPOrgPluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.InstallSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.InstalledSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.SearchPluginDirectoryPayload;
import org.sitebay.android.fluxc.store.PluginStore.SearchedPluginDirectoryPayload;
import org.sitebay.android.fluxc.store.PluginStore.UpdateSitePluginPayload;
import org.sitebay.android.fluxc.store.PluginStore.UpdatedSitePluginPayload;

@ActionEnum
public enum PluginAction implements IAction {
    // Remote actions
    @Action(payloadType = ConfigureSitePluginPayload.class)
    CONFIGURE_SITE_PLUGIN,
    @Action(payloadType = DeleteSitePluginPayload.class)
    DELETE_SITE_PLUGIN,
    @Action(payloadType = FetchPluginDirectoryPayload.class)
    FETCH_PLUGIN_DIRECTORY,
    @Action(payloadType = String.class)
    FETCH_WPORG_PLUGIN,
    @Action(payloadType = InstallSitePluginPayload.class)
    INSTALL_SITE_PLUGIN,
    @Action(payloadType = SearchPluginDirectoryPayload.class)
    SEARCH_PLUGIN_DIRECTORY,
    @Action(payloadType = UpdateSitePluginPayload.class)
    UPDATE_SITE_PLUGIN,

    // Remote responses
    @Action(payloadType = ConfiguredSitePluginPayload.class)
    CONFIGURED_SITE_PLUGIN,
    @Action(payloadType = DeletedSitePluginPayload.class)
    DELETED_SITE_PLUGIN,
    @Action(payloadType = FetchedPluginDirectoryPayload.class)
    FETCHED_PLUGIN_DIRECTORY,
    @Action(payloadType = FetchedWPOrgPluginPayload.class)
    FETCHED_WPORG_PLUGIN,
    @Action(payloadType = InstalledSitePluginPayload.class)
    INSTALLED_SITE_PLUGIN,
    @Action(payloadType = SearchedPluginDirectoryPayload.class)
    SEARCHED_PLUGIN_DIRECTORY,
    @Action(payloadType = UpdatedSitePluginPayload.class)
    UPDATED_SITE_PLUGIN,

    // Local actions
    @Action(payloadType = SiteModel.class)
    REMOVE_SITE_PLUGINS
}
