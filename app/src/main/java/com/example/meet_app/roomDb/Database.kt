package com.example.meet_app.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.meet_app.api.user.UserConverter
import com.example.meet_app.api.user.UserEntity
import com.example.meet_app.roomDb.dao.UserDao


@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
@TypeConverters(UserConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}