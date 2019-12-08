package com.yu.zz.timer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("select * from $TABLE_NAME_TIME_RECORD")
    fun recordList(): MutableList<RecordBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(vararg record: RecordBean)
}