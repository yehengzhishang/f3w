package com.yu.zz.up

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOne(entity: TodoEntity): Long?

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateOne(entity: TodoEntity)

    @Query("select * from todo_table where  list_id = :idList")
    fun queryByListId(idList: String): List<TodoEntity>
}

@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOne(entity: ListEntity): Long?

    @Query("select * from list_table where group_id = null")
    fun queryPersistentNoGroup(): Flowable<List<ListEntity>>
}

@Dao
interface GroupDao {
    @Query("select * from group_table")
    fun queryPersistentAll(): Flowable<List<GroupEntity>>
}