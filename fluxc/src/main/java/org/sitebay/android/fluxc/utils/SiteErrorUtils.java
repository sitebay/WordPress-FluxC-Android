package org.sitebay.android.fluxc.utils;

import org.sitebay.android.fluxc.network.BaseRequest.BaseNetworkError;
import org.sitebay.android.fluxc.store.SiteStore.SiteError;
import org.sitebay.android.fluxc.store.SiteStore.SiteErrorType;

public class SiteErrorUtils {
    public static SiteError genericToSiteError(BaseNetworkError error) {
        SiteErrorType errorType = SiteErrorType.GENERIC_ERROR;
        if (error.isGeneric()) {
            switch (error.type) {
                case INVALID_RESPONSE:
                    errorType = SiteErrorType.INVALID_RESPONSE;
                    break;
            }
        }
        return new SiteError(errorType, error.message);
    }
}
