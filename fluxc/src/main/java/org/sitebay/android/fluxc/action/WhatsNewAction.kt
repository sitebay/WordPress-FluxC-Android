package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction
import org.sitebay.android.fluxc.store.WhatsNewStore.WhatsNewFetchPayload

@ActionEnum
enum class WhatsNewAction : IAction {
    // Remote actions
    @Action(payloadType = WhatsNewFetchPayload::class)
    FETCH_REMOTE_ANNOUNCEMENT,

    @Action
    FETCH_CACHED_ANNOUNCEMENT
}
