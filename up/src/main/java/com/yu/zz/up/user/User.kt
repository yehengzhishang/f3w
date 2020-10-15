package com.yu.zz.up.user

import androidx.room.*
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.up.R
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

@Entity(tableName = "user_table")
class UserEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var name: String? = null
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(user: UserEntity): Long?

    @Query("select * from user_table")
    fun queryAll(): Flowable<List<UserEntity>>
}

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}

class UserActivity : BaseActivity() {
    private val database: UserDataBase by lazy {
        Room.databaseBuilder(applicationContext, UserDataBase::class.java, "up").build()
    }

    private val dao: UserDao by lazy {
        database.getUserDao()
    }

    override fun layoutId(): Int {
        return R.layout.user_zz_activity_zz_test;

    }

    override fun createSecondUi() {

    }

    override fun createThirdData() {
        dao.queryAll()
                .goToThreadMain()
                .subscribe(object : FlowableSubscriber<List<UserEntity>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription) {

                    }

                    override fun onNext(t: List<UserEntity>) {

                    }

                    override fun onError(t: Throwable?) {

                    }
                })
    }
}