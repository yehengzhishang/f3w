package com.yu.zz.up

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOne(entity: TodoEntity): Single<Long>

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateOne(entity: TodoEntity): Completable

    @Query("select * from todo_table where  list_id = :idList")
    fun queryByListId(idList: String): List<TodoEntity>

    @Query("select * from todo_table")
    fun queryPersistentAll(): Flowable<List<TodoEntity>>
}

@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertOne(entity: ListEntity): Single<Long>

    @Query("select * from list_table where group_id = null")
    fun queryPersistentNoGroup(): Flowable<List<ListEntity>>
}

@Dao
interface GroupDao {
    @Query("select * from group_table")
    fun queryPersistentAll(): Flowable<List<GroupEntity>>
}