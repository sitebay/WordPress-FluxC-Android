package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.network.discovery.SelfHostedEndpointFinder.DiscoveryResultPayload;
import org.sitebay.android.fluxc.network.rest.wpcom.auth.Authenticator.AuthEmailResponsePayload;
import org.sitebay.android.fluxc.store.AccountStore.AuthEmailPayload;
import org.sitebay.android.fluxc.store.AccountStore.AuthenticateErrorPayload;
import org.sitebay.android.fluxc.store.AccountStore.AuthenticatePayload;

@ActionEnum
public enum AuthenticationAction implements IAction {
    // Remote actions
    @Action(payloadType = AuthenticatePayload.class)
    AUTHENTICATE,
    @Action(payloadType = String.class)
    DISCOVER_ENDPOINT,
    @Action(payloadType = AuthEmailPayload.class)
    SEND_AUTH_EMAIL,

    // Remote responses
    @Action(payloadType = AuthenticateErrorPayload.class)
    AUTHENTICATE_ERROR,
    @Action(payloadType = DiscoveryResultPayload.class)
    DISCOVERY_RESULT,
    @Action(payloadType = AuthEmailResponsePayload.class)
    SENT_AUTH_EMAIL
}
