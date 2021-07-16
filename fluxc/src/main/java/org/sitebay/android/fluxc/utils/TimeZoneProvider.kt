package org.sitebay.android.fluxc.utils

import java.util.TimeZone
import javax.inject.Inject

class TimeZoneProvider @Inject constructor() {
    fun getDefaultTimeZone(): TimeZone = TimeZone.getDefault()
}
