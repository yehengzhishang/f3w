package com.yu.zz.up

class TodoEntity {
    var id: Long = 0
    var parentId: Long? = null
    var createTime: Long = -1
    var title: String = ""

    var timeStartPlan: Long? = null
    var timeEndPlan: Long? = null
    var timeStart:Long?=null
    var timeEnd:Long?=null

}