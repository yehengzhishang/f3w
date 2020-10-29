package com.yu.zz.up

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val DEFAULT_GROUP = "task"
const val TODO_TABLE_NAME = "todo_table"
const val TODO_COLUMN_ID = "todo_id"
const val TODO_COLUMN_ID_PARENT = "parent_id"
const val TODO_COLUMN_LEVEL = "level"
const val TODO_COLUMN_CREATE_TIME = "create_time"
const val TODO_COLUMN_TITLE = "title"
const val TODO_COLUMN_TIME_START_PLAN = "time_start_plan"
const val TODO_COLUMN_TIME_END_PLAN = "time_end_plan"
const val TODO_COLUMN_TIME_START = "start_time"
const val TODO_COLUMN_TIME_END = "end_time"
const val TODO_COLUMN_TIME_REMIND = "remind_time"
const val TODO_COLUMN_COLOR_TAG = "color_tag"
const val TODO_COLUMN_TAG = "tag"
const val TODO_COLUMN_ID_LIST = "list_id"
const val TODO_COLUMN_TIME_UPDATE = "update_time"

@Entity(tableName = TODO_TABLE_NAME)
class TodoEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TODO_COLUMN_ID)
    var todoId: Long = 0

    // 直属父id
    @ColumnInfo(name = TODO_COLUMN_ID_PARENT)
    var parentId: Long? = null

    // 所有父ID ","分隔
    @ColumnInfo(name = TODO_COLUMN_LEVEL)
    var level: String? = null

    //创建时间
    @ColumnInfo(name = TODO_COLUMN_CREATE_TIME)
    var createTime: Long = -1

    // 名称
    @ColumnInfo(name = TODO_COLUMN_TITLE)
    var title: String = ""

    // 计划时间
    @ColumnInfo(name = TODO_COLUMN_TIME_START_PLAN)
    var timeStartPlan: Long? = null

    @ColumnInfo(name = TODO_COLUMN_TIME_END_PLAN)
    var timeEndPlan: Long? = null

    // 实际时间
    @ColumnInfo(name = TODO_COLUMN_TIME_START)
    var timeStart: Long? = null

    @ColumnInfo(name = TODO_COLUMN_TIME_END)
    var timeEnd: Long? = null

    //提醒时间
    @ColumnInfo(name = TODO_COLUMN_TIME_REMIND)
    var timeRemind: Long? = null

    // 颜色标识
    @ColumnInfo(name = TODO_COLUMN_COLOR_TAG)
    var colorTag: String? = null

    // 标签
    @ColumnInfo(name = TODO_COLUMN_TAG)
    var tag: String? = null

    //分组id
    @ColumnInfo(name = TODO_COLUMN_ID_LIST)
    var listId: String = DEFAULT_GROUP

    @ColumnInfo(name = TODO_COLUMN_TIME_UPDATE)
    var timeUpdate: Long = 0
}

@Entity(tableName = "color_table")
class ColorEntity {
    @PrimaryKey
    @ColumnInfo(name = "color")
    var color: String? = null
}

@Entity(tableName = "group_table")
class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    var groupId: Long = 0

    @ColumnInfo(name = "group_name")
    var groupName: String? = null
}

const val TIME_NAME = "time_table"

@Entity(tableName = TIME_NAME)
class TimeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_id")
    var timeId: Long = 0

    @ColumnInfo(name = TODO_COLUMN_ID)
    var todoId: Long? = null

    @ColumnInfo(name = "start_time")
    var timeStart: Long? = null

    @ColumnInfo(name = "end_time")
    var timeEnd: Long? = null
}

@Entity(tableName = "list_table")
class ListEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    var listId: Long = 0

    @ColumnInfo(name = "list_name")
    var listName: String? = null

    @ColumnInfo(name = "group_id")
    var groupId: Long? = null
}