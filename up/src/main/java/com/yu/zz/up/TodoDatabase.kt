package com.yu.zz.up

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TodoEntity::class, ListEntity::class, GroupEntity::class],
    exportSchema = false,
    version = 1,
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getDaoTodo(): TodoDao
    abstract fun getDaoList(): ListDao
    abstract fun getDaoGroup(): GroupDao
}

fun todoDataBase(context: Context): TodoDatabase {
    return Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "todo_db")
        .build()
}