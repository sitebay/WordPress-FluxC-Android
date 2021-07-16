package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.SiteModel;
import org.sitebay.android.fluxc.model.SitesModel;
import org.sitebay.android.fluxc.network.rest.wpcom.site.SiteRestClient.DeleteSiteResponsePayload;
import org.sitebay.android.fluxc.network.rest.wpcom.site.SiteRestClient.ExportSiteResponsePayload;
import org.sitebay.android.fluxc.network.rest.wpcom.site.SiteRestClient.FetchWPComSiteResponsePayload;
import org.sitebay.android.fluxc.network.rest.wpcom.site.SiteRestClient.IsWPComResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.AutomatedTransferEligibilityResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.AutomatedTransferStatusResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.CompleteQuickStartPayload;
import org.sitebay.android.fluxc.store.SiteStore.ConnectSiteInfoPayload;
import org.sitebay.android.fluxc.store.SiteStore.DesignateMobileEditorForAllSitesPayload;
import org.sitebay.android.fluxc.store.SiteStore.DesignateMobileEditorForAllSitesResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.DesignateMobileEditorPayload;
import org.sitebay.android.fluxc.store.SiteStore.DesignatePrimaryDomainPayload;
import org.sitebay.android.fluxc.store.SiteStore.DesignatedPrimaryDomainPayload;
import org.sitebay.android.fluxc.store.SiteStore.DomainAvailabilityResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.DomainSupportedCountriesResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.DomainSupportedStatesResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchBlockLayoutsPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchJetpackCapabilitiesPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchPrivateAtomicCookiePayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchSitesPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedBlockLayoutsResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedEditorsPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedJetpackCapabilitiesPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedPlansPayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedPrivateAtomicCookiePayload;
import org.sitebay.android.fluxc.store.SiteStore.FetchedUserRolesPayload;
import org.sitebay.android.fluxc.store.SiteStore.InitiateAutomatedTransferPayload;
import org.sitebay.android.fluxc.store.SiteStore.InitiateAutomatedTransferResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.NewSitePayload;
import org.sitebay.android.fluxc.store.SiteStore.QuickStartCompletedResponsePayload;
import org.sitebay.android.fluxc.store.SiteStore.RefreshSitesXMLRPCPayload;
import org.sitebay.android.fluxc.store.SiteStore.SuggestDomainsPayload;
import org.sitebay.android.fluxc.store.SiteStore.SuggestDomainsResponsePayload;

@ActionEnum
public enum SiteAction implements IAction {
    // Remote actions
    @Action(payloadType = SiteModel.class)
    FETCH_PROFILE_XML_RPC,
    @Action(payloadType = SiteModel.class)
    FETCH_SITE,
    @Action(payloadType = FetchSitesPayload.class)
    FETCH_SITES,
    @Action(payloadType = RefreshSitesXMLRPCPayload.class)
    FETCH_SITES_XML_RPC,
    @Action(payloadType = NewSitePayload.class)
    CREATE_NEW_SITE,
    @Action(payloadType = SiteModel.class)
    FETCH_POST_FORMATS,
    @Action(payloadType = SiteModel.class)
    FETCH_SITE_EDITORS,
    @Action(payloadType = DesignateMobileEditorPayload.class)
    DESIGNATE_MOBILE_EDITOR,
    @Action(payloadType = DesignateMobileEditorForAllSitesPayload.class)
    DESIGNATE_MOBILE_EDITOR_FOR_ALL_SITES,
    @Action(payloadType = SiteModel.class)
    FETCH_USER_ROLES,
    @Action(payloadType = SiteModel.class)
    DELETE_SITE,
    @Action(payloadType = SiteModel.class)
    EXPORT_SITE,
    @Action(payloadType = String.class)
    IS_WPCOM_URL,
    @Action(payloadType = SuggestDomainsPayload.class)
    SUGGEST_DOMAINS,
    @Action(payloadType = String.class)
    FETCH_CONNECT_SITE_INFO,
    @Action(payloadType = String.class)
    FETCH_WPCOM_SITE_BY_URL,
    @Action(payloadType = SiteModel.class)
    CHECK_AUTOMATED_TRANSFER_ELIGIBILITY,
    @Action(payloadType = InitiateAutomatedTransferPayload.class)
    INITIATE_AUTOMATED_TRANSFER,
    @Action(payloadType = SiteModel.class)
    CHECK_AUTOMATED_TRANSFER_STATUS,
    @Action(payloadType = SiteModel.class)
    FETCH_PLANS,
    @Action(payloadType = String.class)
    CHECK_DOMAIN_AVAILABILITY,
    @Action(payloadType = String.class)
    FETCH_DOMAIN_SUPPORTED_STATES,
    @Action
    FETCH_DOMAIN_SUPPORTED_COUNTRIES,
    @Action(payloadType = CompleteQuickStartPayload.class)
    COMPLETE_QUICK_START,
    @Action(payloadType = DesignatePrimaryDomainPayload.class)
    DESIGNATE_PRIMARY_DOMAIN,
    @Action(payloadType = FetchPrivateAtomicCookiePayload.class)
    FETCH_PRIVATE_ATOMIC_COOKIE,
    @Action(payloadType = FetchBlockLayoutsPayload.class)
    FETCH_BLOCK_LAYOUTS,

    // Remote responses
    @Action(payloadType = SiteModel.class)
    FETCHED_PROFILE_XML_RPC,
    @Action(payloadType = FetchedEditorsPayload.class)
    FETCHED_SITE_EDITORS,
    @Action(payloadType = DesignateMobileEditorForAllSitesResponsePayload.class)
    DESIGNATED_MOBILE_EDITOR_FOR_ALL_SITES,
    @Action(payloadType = FetchedUserRolesPayload.class)
    FETCHED_USER_ROLES,
    @Action(payloadType = DeleteSiteResponsePayload.class)
    DELETED_SITE,
    @Action(payloadType = ExportSiteResponsePayload.class)
    EXPORTED_SITE,
    @Action(payloadType = ConnectSiteInfoPayload.class)
    FETCHED_CONNECT_SITE_INFO,
    @Action(payloadType = FetchWPComSiteResponsePayload.class)
    FETCHED_WPCOM_SITE_BY_URL,
    @Action(payloadType = AutomatedTransferEligibilityResponsePayload.class)
    CHECKED_AUTOMATED_TRANSFER_ELIGIBILITY,
    @Action(payloadType = InitiateAutomatedTransferResponsePayload.class)
    INITIATED_AUTOMATED_TRANSFER,
    @Action(payloadType = AutomatedTransferStatusResponsePayload.class)
    CHECKED_AUTOMATED_TRANSFER_STATUS,
    @Action(payloadType = FetchedPlansPayload.class)
    FETCHED_PLANS,
    @Action(payloadType = DomainAvailabilityResponsePayload.class)
    CHECKED_DOMAIN_AVAILABILITY,
    @Action(payloadType = DomainSupportedStatesResponsePayload.class)
    FETCHED_DOMAIN_SUPPORTED_STATES,
    @Action(payloadType = DomainSupportedCountriesResponsePayload.class)
    FETCHED_DOMAIN_SUPPORTED_COUNTRIES,
    @Action(payloadType = QuickStartCompletedResponsePayload.class)
    COMPLETED_QUICK_START,
    @Action(payloadType = DesignatedPrimaryDomainPayload.class)
    DESIGNATED_PRIMARY_DOMAIN,
    @Action(payloadType = FetchedPrivateAtomicCookiePayload.class)
    FETCHED_PRIVATE_ATOMIC_COOKIE,
    @Action(payloadType = FetchJetpackCapabilitiesPayload.class)
    FETCH_JETPACK_CAPABILITIES,
    @Action(payloadType = FetchedJetpackCapabilitiesPayload.class)
    FETCHED_JETPACK_CAPABILITIES,
    @Action(payloadType = FetchedBlockLayoutsResponsePayload.class)
    FETCHED_BLOCK_LAYOUTS,

    // Local actions
    @Action(payloadType = SiteModel.class)
    UPDATE_SITE,
    @Action(payloadType = SitesModel.class)
    UPDATE_SITES,
    @Action(payloadType = SiteModel.class)
    REMOVE_SITE,
    @Action
    REMOVE_ALL_SITES,
    @Action
    REMOVE_WPCOM_AND_JETPACK_SITES,
    @Action(payloadType = SitesModel.class)
    SHOW_SITES,
    @Action(payloadType = SitesModel.class)
    HIDE_SITES,
    @Action(payloadType = IsWPComResponsePayload.class)
    CHECKED_IS_WPCOM_URL,
    @Action(payloadType = SuggestDomainsResponsePayload.class)
    SUGGESTED_DOMAINS,
}
