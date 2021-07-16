package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.store.ActivityLogStore;

@ActionEnum
public enum ActivityLogAction implements IAction {
    // Remote actions
    @Action(payloadType = ActivityLogStore.FetchActivityLogPayload.class)
    FETCH_ACTIVITIES,
    @Action(payloadType = ActivityLogStore.FetchRewindStatePayload.class)
    FETCH_REWIND_STATE,
    @Action(payloadType = ActivityLogStore.RewindPayload.class)
    REWIND,
    @Action(payloadType = ActivityLogStore.BackupDownloadPayload.class)
    BACKUP_DOWNLOAD,
    @Action(payloadType = ActivityLogStore.FetchBackupDownloadStatePayload.class)
    FETCH_BACKUP_DOWNLOAD_STATE,
    @Action(payloadType = ActivityLogStore.FetchActivityTypesPayload.class)
    FETCH_ACTIVITY_TYPES,
    @Action(payloadType = ActivityLogStore.DismissBackupDownloadPayload.class)
    DISMISS_BACKUP_DOWNLOAD
}
