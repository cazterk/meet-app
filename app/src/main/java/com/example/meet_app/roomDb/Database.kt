package com.example.meet_app.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.meet_app.api.user.UserEntity
import com.example.meet_app.roomDb.dao.UserDao


@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}