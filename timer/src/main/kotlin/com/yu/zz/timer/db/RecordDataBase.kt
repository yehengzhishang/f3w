package com.yu.zz.timer.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecordBean::class], version = 1)
abstract class RecordDataBase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}