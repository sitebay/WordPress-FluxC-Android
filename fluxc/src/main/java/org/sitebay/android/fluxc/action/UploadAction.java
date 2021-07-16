package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.model.PostModel;
import org.sitebay.android.fluxc.store.MediaStore.ProgressPayload;
import org.sitebay.android.fluxc.store.PostStore.RemoteAutoSavePostPayload;
import org.sitebay.android.fluxc.store.PostStore.RemotePostPayload;
import org.sitebay.android.fluxc.store.UploadStore.ClearMediaPayload;

@ActionEnum
public enum UploadAction implements IAction {
    // Remote responses
    @Action(payloadType = ProgressPayload.class)
    UPLOADED_MEDIA, // Proxy for MediaAction.UPLOADED_MEDIA
    @Action(payloadType = RemotePostPayload.class)
    PUSHED_POST, // Proxy for PostAction.PUSHED_POST
    @Action(payloadType = RemoteAutoSavePostPayload.class)
    REMOTE_AUTO_SAVED_POST, // Proxy for PostAction.REMOTE_AUTO_SAVED_POST

    // Local actions
    @Action(payloadType = PostModel.class)
    INCREMENT_NUMBER_OF_AUTO_UPLOAD_ATTEMPTS,
    @Action(payloadType = PostModel.class)
    CANCEL_POST,
    @Action(payloadType = ClearMediaPayload.class)
    CLEAR_MEDIA_FOR_POST
}
