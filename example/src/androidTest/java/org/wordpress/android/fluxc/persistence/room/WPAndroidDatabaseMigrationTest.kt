package org.wordpress.android.fluxc.persistence.room

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase.Companion.MIGRATION_1_2
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase.Companion.MIGRATION_2_3
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase.Companion.MIGRATION_3_4
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase.Companion.MIGRATION_7_8
import org.wordpress.android.fluxc.persistence.WPAndroidDatabase.Companion.WP_DB_NAME
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WPAndroidDatabaseMigrationTest {
    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WPAndroidDatabase::class.java,
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        helper.createDatabase(WP_DB_NAME, 1).apply {
            // populate BloggingReminders with some data
            execSQL(
                """
                INSERT INTO BloggingReminders 
                (localSiteId, monday, tuesday, wednesday, thursday, friday, saturday, sunday) 
                VALUES (1000,1, 1, 0, 0, 1, 0, 1) 
                 """
            )
            close()
        }

        // Re-open the database with version 3 and provide migration to check against.
        // Ask MigrationTestHelper to verify the schema changes
        val db = helper.runMigrationsAndValidate(WP_DB_NAME, 2, true, MIGRATION_1_2)

        // Validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM BloggingReminders")

        assertThat(cursor.count).isEqualTo(1)
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("localSiteId"))).isEqualTo(1000)
        assertThat(cursor.getInt(cursor.getColumnIndex("tuesday"))).isEqualTo(1)
        assertThat(cursor.getInt(cursor.getColumnIndex("thursday"))).isEqualTo(0)
        cursor.close()
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        helper.createDatabase(WP_DB_NAME, 2).apply {
            // populate BloggingReminders with some data
            execSQL(
                """ 
                INSERT INTO BloggingReminders 
                (localSiteId, monday, tuesday, wednesday, thursday, friday, saturday, sunday) 
                VALUES (1000,1, 1, 0, 0, 1, 0, 1) 
            """
            )
            close()
        }

        // Re-open the database with version 3 and provide migration to check against.
        // Ask MigrationTestHelper to verify the schema changes
        val db = helper.runMigrationsAndValidate(WP_DB_NAME, 3, true, MIGRATION_2_3)

        // Validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM BloggingReminders")

        assertThat(cursor.count).isEqualTo(1)
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("localSiteId"))).isEqualTo(1000)
        assertThat(cursor.getInt(cursor.getColumnIndex("tuesday"))).isEqualTo(1)
        assertThat(cursor.getInt(cursor.getColumnIndex("thursday"))).isEqualTo(0)
        cursor.close()
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate3To4() {
        val oldVersion = 3
        val newVersion = 4

        helper.createDatabase(WP_DB_NAME, oldVersion).apply {
            // populate BloggingReminders with some data
            execSQL(
                """ 
                INSERT INTO BloggingReminders 
                (localSiteId, monday, tuesday, wednesday, thursday, friday, saturday, sunday) 
                VALUES (1000,1, 1, 0, 0, 1, 0, 1) 
            """
            )
            close()
        }

        // Re-open the database with the new version and provide migration to check against.
        val db = helper.runMigrationsAndValidate(WP_DB_NAME, newVersion, true, MIGRATION_3_4)

        // Validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM BloggingReminders")

        assertThat(cursor.count).isEqualTo(1)
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("localSiteId"))).isEqualTo(1000)
        assertThat(cursor.getInt(cursor.getColumnIndex("tuesday"))).isEqualTo(1)
        assertThat(cursor.getInt(cursor.getColumnIndex("thursday"))).isEqualTo(0)
        // also test existing entry has new field hour with correct default (10)
        assertThat(cursor.getInt(cursor.getColumnIndex("hour"))).isEqualTo(10)
        // also test existing entry has new field minute with correct default (0)
        assertThat(cursor.getInt(cursor.getColumnIndex("minute"))).isEqualTo(0)
        cursor.close()
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate7To8() {
        val oldVersion = 7
        val newVersion = 8

        helper.createDatabase(WP_DB_NAME, oldVersion).apply {
            // populate BloggingReminders with some data
            execSQL(
                """ 
                INSERT INTO BloggingReminders 
                (localSiteId, monday, tuesday, wednesday, thursday, friday, saturday, sunday, hour, minute) 
                VALUES (1000,1, 1, 0, 0, 1, 0, 1, 10, 33) 
            """
            )
            close()
        }

        // Re-open the database with the new version and provide migration to check against.
        val db = helper.runMigrationsAndValidate(WP_DB_NAME, newVersion, true, MIGRATION_7_8)

        // Validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM BloggingReminders")

        assertThat(cursor.count).isEqualTo(1)
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("localSiteId"))).isEqualTo(1000)
        assertThat(cursor.getInt(cursor.getColumnIndex("tuesday"))).isEqualTo(1)
        assertThat(cursor.getInt(cursor.getColumnIndex("thursday"))).isEqualTo(0)
        assertThat(cursor.getInt(cursor.getColumnIndex("hour"))).isEqualTo(10)
        assertThat(cursor.getInt(cursor.getColumnIndex("minute"))).isEqualTo(33)
        // also test existing entry has new field isPromptRemindersOptedIn with correct default (0)
        assertThat(cursor.getInt(cursor.getColumnIndex("isPromptRemindersOptedIn"))).isEqualTo(0)
        cursor.close()
        db.close()
    }

    @Test
    @Ignore("Disabling as a part of effort to exclude flaky or failing tests." +
        "See https://github.com/wordpress-mobile/WordPress-FluxC-Android/pull/2665")
    @Throws(IOException::class)
    fun migrate10to11() {
        val oldVersion = 10
        val newVersion = 11
        helper.createDatabase(WP_DB_NAME, oldVersion).apply {
            // populate BloggingReminders with some data
            execSQL(
                """ 
                INSERT INTO BloggingReminders 
                (localSiteId, monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                hour, minute, isPromptRemindersOptedIn) 
                VALUES (1000,1, 1, 0, 0, 1, 0, 1, 10, 33, 0) 
            """.trimIndent()
            )
            close()
        }

        // Re-open the database with the new version and provide migration to check against.
        val db = helper.runMigrationsAndValidate(WP_DB_NAME, newVersion, true)

        // Validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM BloggingReminders")

        assertThat(cursor.count).isEqualTo(1)
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("localSiteId"))).isEqualTo(1000)
        assertThat(cursor.getInt(cursor.getColumnIndex("tuesday"))).isEqualTo(1)
        assertThat(cursor.getInt(cursor.getColumnIndex("thursday"))).isEqualTo(0)
        assertThat(cursor.getInt(cursor.getColumnIndex("hour"))).isEqualTo(10)
        assertThat(cursor.getInt(cursor.getColumnIndex("minute"))).isEqualTo(33)
        assertThat(cursor.getInt(cursor.getColumnIndex("isPromptRemindersOptedIn"))).isEqualTo(0)
        // also test existing entry has new field isPromptsCardOptedIn with correct default (1)
        assertThat(cursor.getInt(cursor.getColumnIndex("isPromptsCardOptedIn"))).isEqualTo(1)

        cursor.close()
        db.close()
    }
}
