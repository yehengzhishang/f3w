package com.yu.zz.up

import com.yu.zz.bypass.goToThreadIO
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class TodoRepository constructor(private val dao: TodoDao) {
    fun create(todo: TodoEntity) = dao.insertOne(todo)

    fun queryAll() = dao.queryPersistentAll()
}

class TodoCache constructor(private val map: Map<String, TodoEntity>) : Map<String, TodoEntity> by map {

}

class GradationRepository(private val daoList: ListDao, private val daoGroup: GroupDao) {
    fun loadAll(): Flowable<List<GradationInfo>> {
        return Flowable.zip(loadGroupAll(), loadListAll(), GradationInfoZip())
    }

    private fun loadGroupAll() = daoGroup.queryPersistentAll().goToThreadIO()

    private fun loadListAll() = daoList.queryPersistentNoGroup().goToThreadIO()
}

class GradationInfoZip : BiFunction<List<GroupEntity>, List<ListEntity>, List<GradationInfo>> {
    override fun apply(lists: List<GroupEntity>, groups: List<ListEntity>): List<GradationInfo> {
        return mutableListOf<GradationInfo>().apply {
            for (list in lists) {
                add(GradationInfo(list))
            }
            for (group in groups) {
                add(GradationInfo(group))
            }
        }
    }
}

class GradationInfo(private val entity: Any) {
    fun isList(): Boolean {
        return entity is ListEntity
    }

    fun isGroup(): Boolean {
        return entity is GroupEntity
    }

    fun getList(): ListEntity {
        return entity as ListEntity
    }

    fun getGroup(): GroupEntity {
        return entity as GroupEntity
    }
}