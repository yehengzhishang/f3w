package com.yu.zz.up

class TodoRepository {
    fun queryOne(todoId: String): TodoEntity {
        return TodoEntity()
    }
}


class TodoCache constructor(val map: Map<String, TodoEntity>) : Map<String, TodoEntity> by map {


}
