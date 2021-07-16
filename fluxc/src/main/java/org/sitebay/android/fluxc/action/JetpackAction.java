package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.SiteModel;

@ActionEnum
public enum JetpackAction implements IAction {
    @Action(payloadType = SiteModel.class)
    INSTALL_JETPACK
}
