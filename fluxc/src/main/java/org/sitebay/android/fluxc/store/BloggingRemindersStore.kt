package org.sitebay.android.fluxc.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.sitebay.android.fluxc.model.BloggingRemindersMapper
import org.sitebay.android.fluxc.model.BloggingRemindersModel
import org.sitebay.android.fluxc.persistence.BloggingRemindersDao
import org.sitebay.android.fluxc.tools.CoroutineEngine
import org.sitebay.android.util.AppLog.T
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BloggingRemindersStore
@Inject constructor(
    private val bloggingRemindersDao: BloggingRemindersDao,
    private val mapper: BloggingRemindersMapper,
    private val coroutineEngine: CoroutineEngine
) {
    fun bloggingRemindersModel(siteId: Int): Flow<BloggingRemindersModel> {
        return bloggingRemindersDao.getBySiteId(siteId).map {
            it?.let { dbModel -> mapper.toDomainModel(dbModel) } ?: BloggingRemindersModel(siteId)
        }
    }

    suspend fun updateBloggingReminders(model: BloggingRemindersModel) =
            coroutineEngine.withDefaultContext(T.SETTINGS, this, "Updating blogging reminders") {
                bloggingRemindersDao.insert(mapper.toDatabaseModel(model))
            }
}
