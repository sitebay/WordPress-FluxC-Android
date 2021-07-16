package org.sitebay.android.fluxc.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.sitebay.android.fluxc.persistence.BloggingRemindersDao
import org.sitebay.android.fluxc.persistence.WPAndroidDatabase
import org.sitebay.android.fluxc.persistence.WPAndroidDatabase.Companion.buildDb
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton @Provides fun provideDatabase(context: Context): WPAndroidDatabase {
        return buildDb(context)
    }

    @Singleton @Provides fun provideBloggingRemindersDao(wpAndroidDatabase: WPAndroidDatabase): BloggingRemindersDao {
        return wpAndroidDatabase.bloggingRemindersDao()
    }
}
