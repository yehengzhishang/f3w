package com.yu.zz.up

import android.app.Application
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.bypass.add
import com.yu.zz.bypass.goToThreadIO
import com.yu.zz.bypass.goToThreadMain
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class MainUpActivity : UpActivity() {
    private val mViewModel: MainViewModel by lazy {
        createViewModel(viewModelStore, MainViewModelFactory(application,
                todoDataBase(application)), MainViewModel::class.java)
    }
    private val mEtInput: EditText by lazy {
        findViewById<EditText>(R.id.et_crate_input)
    }
    private val mAdapter: MainAdapter = MainAdapter()

    override fun layoutId(): Int {
        return R.layout.up_mian_activity
    }

    override fun createSecondUi() {
        findViewById<TextView>(R.id.tv_create).setOnClickListener { clickCreate() }

        mViewModel.dataListTodo.observe(this, Observer {
            obList(it)
        })

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mAdapter
    }

    override fun createThirdData() {
        mViewModel.loadAllByTodo()
    }

    private fun clickCreate() {
        val name: String = mEtInput.text.toString()
        if (TextUtils.isEmpty(name)) {
            return
        }
        mViewModel.create(name)
    }

    private fun obList(list: List<TodoEntity>?) {
        if (list == null) {
            return
        }
        mAdapter.setData(list)
        mAdapter.notifyDataSetChanged()
    }
}

class MainViewModelFactory(private val app: Application, private val database: TodoDatabase) : ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app, MainRepository(database)) as T
        }
        return super.create(modelClass)
    }
}

class MainViewModel(app: Application, private val repo: MainRepository) : UpViewModel(app) {
    private val mDataListGradation by lazy {
        MutableLiveData<List<GradationInfo>>()
    }
    private val mDataListTodo by lazy {
        MutableLiveData<List<TodoEntity>>()
    }

    val dataListTodo: LiveData<List<TodoEntity>> get() = mDataListTodo

    private fun loadAllByGradation() {
        repo.queryAll()
                .goToThreadMain()
                .subscribe {
                    mDataListGradation.postValue(it)
                }.add(mDisposables)
    }

    fun loadAllByTodo() {
        repo.queryAllTodo()
                .goToThreadIO()
                .subscribe {
                    mDataListTodo.postValue(it)
                }.add(mDisposables)
    }

    fun create(name: String) {
        repo.create(TodoEntity().apply {
            this.title = name
        }).subscribe(object : SingleObserver<Long> {
            override fun onSuccess(t: Long) {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
            }
        })
    }
}


class MainRepository constructor(db: TodoDatabase) {
    private val mRepoTodo: TodoRepository = TodoRepository(db.getDaoTodo())
    private val mRepoGradation: GradationRepository = GradationRepository(db.getDaoList(), db.getDaoGroup())

    fun queryAll() = mRepoGradation.loadAll()
    fun queryAllTodo() = mRepoTodo.queryAll()
    fun create(todoEntity: TodoEntity) = mRepoTodo.create(todoEntity)
}

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {
    private val mList = mutableListOf<TodoEntity>()

    fun setData(list: List<TodoEntity>) {
        mList.clear()
        mList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

}

class MainViewHolder constructor(parent: ViewGroup, layoutId: Int) : UpViewHolder<TodoEntity>(parent, layoutId) {
    private val mTvName = itemView.findViewById<TextView>(R.id.tv_name)

    constructor(parent: ViewGroup) : this(parent, R.layout.up_item_main)

    override fun bind(bean: TodoEntity, position: Int) {
        super.bind(bean, position)
        mTvName.text = bean.title
    }
}