package com.yu.zz.up

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TodoEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    // 直属父id
    @ColumnInfo(name = "parent_id")
    var parentId: Long? = null

    // 所有父ID ","分隔
    @ColumnInfo(name = "level")
    var level: String? = null

    //创建时间
    @ColumnInfo(name = "create_time")
    var createTime: Long = -1

    // 名称
    @ColumnInfo(name = "title")
    var title: String = ""

    // 计划时间
    @ColumnInfo(name = "time_start_plan")
    var timeStartPlan: Long? = null

    @ColumnInfo(name = "time_end_plan")
    var timeEndPlan: Long? = null

    // 实际时间
    @ColumnInfo(name = "time_start")
    var timeStart: Long? = null

    @ColumnInfo(name = "time_end")
    var timeEnd: Long? = null

    //提醒时间
    @ColumnInfo(name = "time_remind")
    var timeRemind: Long? = null

    // 颜色标识
    @ColumnInfo(name = "color_tag")
    var colorTag: String? = null

    // 标签
    @ColumnInfo(name = "tag")
    var tag: String? = null
}

@Entity(tableName = "color_table")
class ColorTag {
    @PrimaryKey
    var color: String? = null
}