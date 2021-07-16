package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction
import org.sitebay.android.fluxc.store.EncryptedLogStore.UploadEncryptedLogPayload

@ActionEnum
enum class EncryptedLogAction : IAction {
    @Action(payloadType = UploadEncryptedLogPayload::class)
    UPLOAD_LOG,
    @Action
    RESET_UPLOAD_STATES
}
