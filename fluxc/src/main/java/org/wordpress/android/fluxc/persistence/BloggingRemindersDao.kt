package org.wordpress.android.fluxc.persistence

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BloggingRemindersDao {
    @Query("SELECT * FROM BloggingReminders")
    abstract fun getAll(): Flow<List<BloggingReminders>>

    @Query("SELECT * FROM BloggingReminders WHERE localSiteId = :siteId")
    abstract fun getBySiteId(siteId: Int): Flow<BloggingReminders?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(type: BloggingReminders): Long

    @Entity(tableName = "BloggingReminders")
    data class BloggingReminders(
        @PrimaryKey
        var localSiteId: Int,
        var monday: Boolean = false,
        var tuesday: Boolean = false,
        var wednesday: Boolean = false,
        var thursday: Boolean = false,
        var friday: Boolean = false,
        var saturday: Boolean = false,
        var sunday: Boolean = false
    )
}
