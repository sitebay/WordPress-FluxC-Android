package org.sitebay.android.fluxc.instaflux;

import org.sitebay.android.fluxc.module.AppContextModule;
import org.sitebay.android.fluxc.module.DatabaseModule;
import org.sitebay.android.fluxc.module.ReleaseNetworkModule;
import org.sitebay.android.fluxc.module.OkHttpClientModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppContextModule.class,
        AppConfigModule.class,
        OkHttpClientModule.class,
        ReleaseNetworkModule.class,
        DatabaseModule.class
})
public interface AppComponent {
    void inject(InstafluxApp application);
    void inject(MainInstafluxActivity homeActivity);
    void inject(PostActivity postActivity);
}
