package org.sitebay.android.fluxc.instaflux;

import org.sitebay.android.fluxc.module.AppContextModule;
import org.sitebay.android.fluxc.module.OkHttpClientModule;
import org.sitebay.android.fluxc.module.ReleaseNetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppContextModule.class,
        AppConfigModule.class,
        OkHttpClientModule.class,
        InterceptorModule.class,
        ReleaseNetworkModule.class
})
public interface AppComponentDebug extends AppComponent {}
