package org.sitebay.android.fluxc.example

import androidx.multidex.MultiDexApplication
import com.yarolegovich.wellsql.WellSql
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.sitebay.android.fluxc.example.di.AppComponent
import org.sitebay.android.fluxc.example.di.DaggerAppComponent
import org.sitebay.android.fluxc.persistence.WellSqlConfig
import javax.inject.Inject

open class ExampleApp : MultiDexApplication(), HasAndroidInjector {
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    protected open val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        val wellSqlConfig = WellSqlConfig(applicationContext, WellSqlConfig.ADDON_WOOCOMMERCE)
        WellSql.init(wellSqlConfig)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
