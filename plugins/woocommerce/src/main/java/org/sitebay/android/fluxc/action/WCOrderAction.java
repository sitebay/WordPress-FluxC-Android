package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.store.WCOrderStore.AddOrderShipmentTrackingPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.AddOrderShipmentTrackingResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.DeleteOrderShipmentTrackingPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.DeleteOrderShipmentTrackingResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchHasOrdersPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchHasOrdersResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderListPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderListResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderNotesPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderNotesResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderShipmentProvidersPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderShipmentProvidersResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderShipmentTrackingsPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderShipmentTrackingsResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderStatusOptionsPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrderStatusOptionsResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersByIdsPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersByIdsResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersCountPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersCountResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchOrdersResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.FetchSingleOrderPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.PostOrderNotePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.RemoteOrderNotePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.RemoteOrderPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.SearchOrdersPayload;
import org.sitebay.android.fluxc.store.WCOrderStore.SearchOrdersResponsePayload;
import org.sitebay.android.fluxc.store.WCOrderStore.UpdateOrderStatusPayload;

@ActionEnum
public enum WCOrderAction implements IAction {
    // Remote actions
    @Action(payloadType = FetchOrdersPayload.class)
    FETCH_ORDERS,
    @Action(payloadType = FetchOrderListPayload.class)
    FETCH_ORDER_LIST,
    @Action(payloadType = FetchOrdersByIdsPayload.class)
    FETCH_ORDERS_BY_IDS,
    @Action(payloadType = FetchOrdersCountPayload.class)
    FETCH_ORDERS_COUNT,
    @Action(payloadType = FetchSingleOrderPayload.class)
    FETCH_SINGLE_ORDER,
    @Action(payloadType = UpdateOrderStatusPayload.class)
    UPDATE_ORDER_STATUS,
    @Action(payloadType = FetchOrderNotesPayload.class)
    FETCH_ORDER_NOTES,
    @Action(payloadType = PostOrderNotePayload.class)
    POST_ORDER_NOTE,
    @Action(payloadType = FetchHasOrdersPayload.class)
    FETCH_HAS_ORDERS,
    @Action(payloadType = SearchOrdersPayload.class)
    SEARCH_ORDERS,
    @Action(payloadType = FetchOrderStatusOptionsPayload.class)
    FETCH_ORDER_STATUS_OPTIONS,
    @Action(payloadType = FetchOrderShipmentTrackingsPayload.class)
    FETCH_ORDER_SHIPMENT_TRACKINGS,
    @Action(payloadType = AddOrderShipmentTrackingPayload.class)
    ADD_ORDER_SHIPMENT_TRACKING,
    @Action(payloadType = DeleteOrderShipmentTrackingPayload.class)
    DELETE_ORDER_SHIPMENT_TRACKING,
    @Action(payloadType = FetchOrderShipmentProvidersPayload.class)
    FETCH_ORDER_SHIPMENT_PROVIDERS,

    // Remote responses
    @Action(payloadType = FetchOrdersResponsePayload.class)
    FETCHED_ORDERS,
    @Action(payloadType = FetchOrderListResponsePayload.class)
    FETCHED_ORDER_LIST,
    @Action(payloadType = FetchOrdersByIdsResponsePayload.class)
    FETCHED_ORDERS_BY_IDS,
    @Action(payloadType = FetchOrdersCountResponsePayload.class)
    FETCHED_ORDERS_COUNT,
    @Action(payloadType = RemoteOrderPayload.class)
    FETCHED_SINGLE_ORDER,
    @Action(payloadType = RemoteOrderPayload.class)
    UPDATED_ORDER_STATUS,
    @Action(payloadType = FetchOrderNotesResponsePayload.class)
    FETCHED_ORDER_NOTES,
    @Action(payloadType = RemoteOrderNotePayload.class)
    POSTED_ORDER_NOTE,
    @Action(payloadType = FetchHasOrdersResponsePayload.class)
    FETCHED_HAS_ORDERS,
    @Action(payloadType = SearchOrdersResponsePayload.class)
    SEARCHED_ORDERS,
    @Action(payloadType = FetchOrderStatusOptionsResponsePayload.class)
    FETCHED_ORDER_STATUS_OPTIONS,
    @Action(payloadType = FetchOrderShipmentTrackingsResponsePayload.class)
    FETCHED_ORDER_SHIPMENT_TRACKINGS,
    @Action(payloadType = AddOrderShipmentTrackingResponsePayload.class)
    ADDED_ORDER_SHIPMENT_TRACKING,
    @Action(payloadType = DeleteOrderShipmentTrackingResponsePayload.class)
    DELETED_ORDER_SHIPMENT_TRACKING,
    @Action(payloadType = FetchOrderShipmentProvidersResponsePayload.class)
    FETCHED_ORDER_SHIPMENT_PROVIDERS
}
