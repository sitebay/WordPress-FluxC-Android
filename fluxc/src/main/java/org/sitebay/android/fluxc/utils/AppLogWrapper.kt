package org.sitebay.android.fluxc.utils

import org.sitebay.android.util.AppLog
import javax.inject.Inject

class AppLogWrapper
@Inject constructor() {
    fun d(tag: AppLog.T, message: String) = AppLog.d(tag, message)
    fun e(tag: AppLog.T, message: String) = AppLog.e(tag, message)
}
