package org.sitebay.android.fluxc.network.rest.wpapi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.sitebay.android.fluxc.Dispatcher;
import org.sitebay.android.fluxc.generated.AuthenticationActionBuilder;
import org.sitebay.android.fluxc.network.BaseRequest;
import org.sitebay.android.fluxc.network.BaseRequest.OnAuthFailedListener;
import org.sitebay.android.fluxc.network.UserAgent;
import org.sitebay.android.fluxc.network.discovery.WPAPIHeadRequest;
import org.sitebay.android.fluxc.store.AccountStore.AuthenticateErrorPayload;

public abstract class BaseWPAPIRestClient {
    private final RequestQueue mRequestQueue;
    private final Dispatcher mDispatcher;
    private UserAgent mUserAgent;

    private OnAuthFailedListener mOnAuthFailedListener;

    public BaseWPAPIRestClient(Dispatcher dispatcher, RequestQueue requestQueue,
                               UserAgent userAgent) {
        mDispatcher = dispatcher;
        mRequestQueue = requestQueue;
        mUserAgent = userAgent;
        mOnAuthFailedListener = new OnAuthFailedListener() {
            @Override
            public void onAuthFailed(AuthenticateErrorPayload authError) {
                mDispatcher.dispatch(AuthenticationActionBuilder.newAuthenticateErrorAction(authError));
            }
        };
    }

    protected Request add(WPAPIGsonRequest request) {
        return mRequestQueue.add(setRequestAuthParams(request));
    }

    protected Request add(WPAPIHeadRequest request) {
        return mRequestQueue.add(setRequestAuthParams(request));
    }

    protected Request add(WPAPIEncodedBodyRequest request) {
        return mRequestQueue.add(setRequestAuthParams(request));
    }

    private BaseRequest setRequestAuthParams(BaseRequest request) {
        request.setOnAuthFailedListener(mOnAuthFailedListener);
        request.setUserAgent(mUserAgent.getUserAgent());
        return request;
    }
}
