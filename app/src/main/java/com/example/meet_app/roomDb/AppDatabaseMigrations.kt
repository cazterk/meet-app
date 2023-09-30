package com.example.meet_app.roomDb

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a temporary table with the new schema
        database.execSQL("CREATE TABLE IF NOT EXISTS temp_current_user (" +
                "username TEXT NOT NULL, " +
                "firstName TEXT NOT NULL, " +
                "lastName TEXT NOT NULL, " +
                "profileImage TEXT, " +
                "id TEXT NOT NULL PRIMARY KEY, " +
                "newField TEXT DEFAULT 'defaultValue' NOT NULL)")

        // Copy the data from the old table to the temporary table
        database.execSQL("INSERT INTO temp_current_user (username, firstName, lastName, profileImage, id, newField)" +
                "SELECT username, firstName, lastName, profileImage, id, '' FROM current_user")

        // Drop the old table
        database.execSQL("DROP TABLE current_user")

        // Rename the temporary table to the new table name
        database.execSQL("ALTER TABLE temp_current_user RENAME TO current_user")
    }
}