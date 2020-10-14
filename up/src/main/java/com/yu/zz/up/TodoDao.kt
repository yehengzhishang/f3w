package com.yu.zz.up

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(entity: TodoEntity): Long?

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    fun updateOne(entity: TodoEntity)
}

