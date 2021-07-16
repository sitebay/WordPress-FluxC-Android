package org.sitebay.android.fluxc.example.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.sitebay.android.fluxc.example.WCOrderListActivity

@Module
internal abstract class WCOrderListActivityModule {
    @ContributesAndroidInjector
    abstract fun provideWCOrderListActivityInjector(): WCOrderListActivity
}
