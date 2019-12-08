package com.yu.zz.timer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_NAME_TIME_RECORD)
class RecordBean {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = " start_time")
    var timeStart: Long = 0
    @ColumnInfo(name = " end_time")
    var timeEnd: Long = 0

    @Ignore
    fun timeKeep(): Long {
        if (timeEnd == TIME_NO_END) {
            return TIME_NO_KEEP
        }
        return timeEnd - timeStart
    }
}