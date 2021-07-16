package org.sitebay.android.fluxc.model;

import androidx.annotation.NonNull;

import org.sitebay.android.fluxc.Payload;
import org.sitebay.android.fluxc.network.BaseRequest.BaseNetworkError;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionsModel extends Payload<BaseNetworkError> {
    private List<SubscriptionModel> mSubscriptions;

    public SubscriptionsModel() {
        mSubscriptions = new ArrayList<>();
    }

    public SubscriptionsModel(@NonNull List<SubscriptionModel> subscriptions) {
        mSubscriptions = subscriptions;
    }

    public List<SubscriptionModel> getSubscriptions() {
        return mSubscriptions;
    }

    public void setSubscriptions(List<SubscriptionModel> subscriptions) {
        this.mSubscriptions = subscriptions;
    }
}
