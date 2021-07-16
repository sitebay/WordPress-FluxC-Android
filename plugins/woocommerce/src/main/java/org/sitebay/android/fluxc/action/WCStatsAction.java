package org.sitebay.android.fluxc.action;

import org.sitebay.android.fluxc.annotations.Action;
import org.sitebay.android.fluxc.annotations.ActionEnum;
import org.sitebay.android.fluxc.annotations.action.IAction;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchNewVisitorStatsPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchNewVisitorStatsResponsePayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchOrderStatsPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchOrderStatsResponsePayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchRevenueStatsAvailabilityPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchRevenueStatsAvailabilityResponsePayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchRevenueStatsPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchRevenueStatsResponsePayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchTopEarnersStatsPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchTopEarnersStatsResponsePayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchVisitorStatsPayload;
import org.sitebay.android.fluxc.store.WCStatsStore.FetchVisitorStatsResponsePayload;

@ActionEnum
public enum WCStatsAction implements IAction {
    // Remote actions
    @Action(payloadType = FetchOrderStatsPayload.class)
    FETCH_ORDER_STATS,

    @Action(payloadType = FetchRevenueStatsPayload.class)
    FETCH_REVENUE_STATS,

    @Action(payloadType = FetchRevenueStatsAvailabilityPayload.class)
    FETCH_REVENUE_STATS_AVAILABILITY,

    @Action(payloadType = FetchVisitorStatsPayload.class)
    FETCH_VISITOR_STATS,

    @Action(payloadType = FetchNewVisitorStatsPayload.class)
    FETCH_NEW_VISITOR_STATS,

    @Action(payloadType = FetchTopEarnersStatsPayload.class)
    FETCH_TOP_EARNERS_STATS,

    // Remote responses
    @Action(payloadType = FetchOrderStatsResponsePayload.class)
    FETCHED_ORDER_STATS,

    @Action(payloadType = FetchRevenueStatsResponsePayload.class)
    FETCHED_REVENUE_STATS,

    @Action(payloadType = FetchRevenueStatsAvailabilityResponsePayload.class)
    FETCHED_REVENUE_STATS_AVAILABILITY,

    @Action(payloadType = FetchVisitorStatsResponsePayload.class)
    FETCHED_VISITOR_STATS,

    @Action(payloadType = FetchNewVisitorStatsResponsePayload.class)
    FETCHED_NEW_VISITOR_STATS,

    @Action(payloadType = FetchTopEarnersStatsResponsePayload.class)
    FETCHED_TOP_EARNERS_STATS
}
