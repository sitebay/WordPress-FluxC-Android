package org.sitebay.android.fluxc.action

import org.sitebay.android.fluxc.annotations.Action
import org.sitebay.android.fluxc.annotations.ActionEnum
import org.sitebay.android.fluxc.annotations.action.IAction
import org.sitebay.android.fluxc.model.list.ListDescriptorTypeIdentifier
import org.sitebay.android.fluxc.store.ListStore.FetchedListItemsPayload
import org.sitebay.android.fluxc.store.ListStore.ListItemsRemovedPayload
import org.sitebay.android.fluxc.store.ListStore.RemoveExpiredListsPayload

@ActionEnum
enum class ListAction : IAction {
    @Action(payloadType = FetchedListItemsPayload::class)
    FETCHED_LIST_ITEMS,
    @Action(payloadType = ListItemsRemovedPayload::class)
    LIST_ITEMS_REMOVED,
    @Action(payloadType = ListDescriptorTypeIdentifier::class)
    LIST_REQUIRES_REFRESH,
    @Action(payloadType = ListDescriptorTypeIdentifier::class)
    LIST_DATA_INVALIDATED,
    @Action(payloadType = RemoveExpiredListsPayload::class)
    REMOVE_EXPIRED_LISTS,
    @Action
    REMOVE_ALL_LISTS
}
