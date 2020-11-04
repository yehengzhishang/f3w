package com.yu.zz.up

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yu.zz.bypass.add
import com.yu.zz.bypass.goToThreadMain

class MainUpActivity : UpActivity() {
    override fun layoutId(): Int {
        return R.layout.up_mian_activity
    }

    override fun createSecondUi() {

    }

    override fun createThirdData() {

    }

}

class MainViewModel(private val app: Application, private val repo: MainRepository) : UpViewModel(app) {
    private val mDataListGradation by lazy {
        MutableLiveData<List<GradationInfo>>()
    }

    private fun loadAllByGradation() {
        repo.queryAll()
                .goToThreadMain()
                .subscribe {

                }.add(mDisposables)
    }
}


class MainRepository constructor(private val db: TodoDatabase) {
    private val mRepoTodo: TodoRepository = TodoRepository(db.getDaoTodo())
    private val mRepoGradation: GradationRepository = GradationRepository(db.getDaoList(), db.getDaoGroup())

    fun queryAll() = mRepoGradation.loadAll()
}