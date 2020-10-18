package com.yu.zz.up

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class, ListEntity::class, GroupEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getDaoTodo(): TodoDao
}