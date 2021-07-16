package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction
import org.sitebay.android.fluxc.store.EditorThemeStore.FetchEditorThemePayload

@ActionEnum
enum class EditorThemeAction : IAction {
    @Action(payloadType = FetchEditorThemePayload::class)
    FETCH_EDITOR_THEME
}
