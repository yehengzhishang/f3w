package com.yu.zz.up.user

import android.util.Log
import androidx.room.*
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.up.R
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.user_zz_activity_zz_test.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

@Entity(tableName = "user_table")
class UserEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long = 0

    @ColumnInfo(name = "user_name")
    var name: String? = null
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(user: UserEntity): Long?

    @Query("select * from user_table")
    fun queryAll(): Flowable<List<UserEntity>>

    @Query("select * from user_table")
    fun all(): List<UserEntity>
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
        btn.setOnClickListener {
            val name = tie_name.text.toString()
            create(UserEntity().apply {
                this.name = name
            })
        }
    }

    override fun createThirdData() {
        Flowable.just("abc")
                .subscribe(object : Subscriber<String> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(s: Subscription?) {

                    }

                    override fun onNext(t: String?) {
                        Log.e("rain", t)
                    }

                    override fun onError(t: Throwable?) {

                    }

                })
        dao.queryAll().subscribe(AllUserFlowableSubscriber())
        dao.queryAll().subscribe(AllUserConsumer())
        dao.queryAll().subscribe(AllUserSubscriber())
        Flowable.just(mutableListOf<UserEntity>()).subscribe(AllUserSubscriber())
    }

    private fun create(user: UserEntity) {
        Thread {
            val id = dao.insertOne(user)
            if (id == null) {
                Log.v("rain", "null")
            } else {
                Log.v("rain", id.toString())
            }
            val list = dao.all()

            Log.v("rain", "u start = = = ")
            Log.v("rain", "id  | name")
            for (u in list) {
                Log.v("rain", "${u.id} | ${u.name}")
            }
            Log.v("rain", "end = = =")

        }.start()
    }

    inner class AllUserSubscriber : Subscriber<List<UserEntity>> {
        override fun onComplete() {
            log("onComplete")
        }

        override fun onSubscribe(s: Subscription?) {
            log("onSubscribe")
            s?.request(1000)
        }

        override fun onNext(t: List<UserEntity>?) {
            log("onNext")
        }

        override fun onError(t: Throwable?) {
            log("onError")
        }

        private fun log(message: String) {
            Log.v("rain", javaClass.simpleName + " || $message")
        }
    }

    inner class AllUserConsumer : Consumer<List<UserEntity>> {
        override fun accept(t: List<UserEntity>?) {
            Log.v("rain", javaClass.simpleName + " accept")
        }
    }

    inner class AllUserFlowableSubscriber : FlowableSubscriber<List<UserEntity>> {
        override fun onComplete() {
            log("onComplete")
        }

        override fun onSubscribe(s: Subscription) {
            log("onSubscribe")

        }

        override fun onNext(t: List<UserEntity>?) {
            log("onNext")
        }

        override fun onError(t: Throwable?) {
            log("onError")
        }

        private fun log(message: String) {
            Log.v("rain", javaClass.simpleName + " || $message")
        }
    }
}