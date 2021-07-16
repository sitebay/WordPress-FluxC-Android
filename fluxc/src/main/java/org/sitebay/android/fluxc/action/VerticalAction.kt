package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction

@ActionEnum
enum class VerticalAction : IAction {
    @Action
    FETCH_SEGMENTS,
}
