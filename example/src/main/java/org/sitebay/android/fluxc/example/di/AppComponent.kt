package org.sitebay.android.fluxc.example.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import org.sitebay.android.fluxc.example.ExampleApp
import org.sitebay.android.fluxc.module.DatabaseModule
import org.sitebay.android.fluxc.module.OkHttpClientModule
import org.sitebay.android.fluxc.module.ReleaseNetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        AppConfigModule::class,
        OkHttpClientModule::class,
        ReleaseNetworkModule::class,
        MainActivityModule::class,
        WCOrderListActivityModule::class,
        DatabaseModule::class))
interface AppComponent : AndroidInjector<ExampleApp> {
    override fun inject(app: ExampleApp)

    // Allows us to inject the application without having to instantiate any modules, and provides the Application
    // in the app graph
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
