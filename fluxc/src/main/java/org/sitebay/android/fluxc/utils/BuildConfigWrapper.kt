package org.sitebay.android.fluxc.utils

import dagger.Reusable
import org.sitebay.android.fluxc.BuildConfig
import javax.inject.Inject

@Reusable
class BuildConfigWrapper @Inject constructor() {
    fun isDebug() = BuildConfig.DEBUG
}
