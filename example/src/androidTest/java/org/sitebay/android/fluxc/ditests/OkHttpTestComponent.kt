package org.sitebay.android.fluxc.ditests

import dagger.Component
import org.sitebay.android.fluxc.module.ReleaseNetworkModule
import org.sitebay.android.fluxc.module.OkHttpClientModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            OkHttpClientModule::class,
            ReleaseNetworkModule::class,
            TestInterceptorModule::class
        ]
)
interface OkHttpTestComponent {
    fun inject(test: OkHttpInjectionTest)
}
