package com.yu.zz.tb

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.common.arrange.getScreenWidth
import com.yu.zz.fwww.R
import com.yu.zz.tb.arrange.goToThreadMain
import com.yu.zz.tb.deep.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_topbook_main.*
import androidx.lifecycle.Observer as OB

class MainTopBookActivity : AppCompatActivity() {
    private val mAdapter = TopBookAdapter()
    private val mViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topbook_main)
        rv.adapter = mAdapter
        rv.layoutManager = GridLayoutManager(this, 2)
        mViewModel.getDataTp().observe(this, OB {
            if (it == null) {
                return@OB
            }
            mAdapter.add(it)
        })
    }

    override fun onStart() {
        super.onStart()
        mViewModel.getPage()
    }
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val mMapCategory = HashMap<String, TopBookPageBean>()
    private val mMapTp = HashMap<String, MutableList<TopBookDataBean>>()
    private val mListCategory = mutableListOf<TopBookPageBean>()
    private val mDataCategory by lazy { MutableLiveData<List<TopBookPageBean>>() }
    private val mDataTp by lazy { MutableLiveData<List<TopBookDataBean>>() }

    fun getPage(start: Int = 0, limit: Int = 20) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getPageConfig(start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<TopBookPageResponseBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookPageResponseBean) {
                        getItems(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    fun getDataCategory(): LiveData<List<TopBookPageBean>> {
        return mDataCategory
    }

    fun getDataTp(): LiveData<List<TopBookDataBean>> {
        return mDataTp
    }

    private fun getItems(bean: TopBookPageResponseBean) {
        Observable.just(bean)
                .filter { it.isSuccess() && it.data != null }
                .map { it.data!! }
                .filter { it.items != null }
                .map { it.items!! }
                .flatMap { Observable.fromIterable(it) }
                .filter { it.categoryId != null }
                .doOnNext { addCategory(it!!) }
                .map { it.categoryId!! }
                .flatMap { getObsItem(it.toString()) }
                .goToThreadMain()
                .subscribe(object : Observer<TopBookResponseBean> {
                    override fun onComplete() {
                        refreshData()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookResponseBean) {
                        val itemId = itemId(t) ?: return
                        if (!mMapCategory.keys.contains(itemId)) {
                            return
                        }
                        var list = mMapTp.getOrPut(itemId, { mutableListOf() })
                        val listSource = t.data?.items!!
                        for (sourceBean in listSource) {
                            if (sourceBean == null) {
                                continue
                            }
                            list.add(sourceBean)
                        }
                        Log.e("rain", GsonBuilder().setPrettyPrinting().create().toJson(t))
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    private fun refreshData() {
        mListCategory.sortBy { it.categoryId }
        mDataCategory.postValue(mListCategory)
        val listAll = mutableListOf<TopBookDataBean>()
        for (category in mListCategory) {
            val list: MutableList<TopBookDataBean>? = mMapTp[category.categoryId!!.toString()]
                    ?: continue
            listAll.addAll(list!!)
        }
        mDataTp.postValue(listAll)
    }

    private fun addCategory(bean: TopBookPageBean) {
        mMapCategory[bean.categoryId!!.toString()] = bean
        mListCategory.add(bean)
    }


    private fun itemId(t: TopBookResponseBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(itemId: String, start: Int = 0, limit: Int = 8): Observable<TopBookResponseBean> {
        return TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getTopBookList(itemId, start.toString(), limit.toString())
    }

    private fun addItem(itemId: String, bean: TopBookResponseBean) {

    }
}

class TopBookAdapter : RecyclerView.Adapter<TopBookViewHolder>() {
    private val mListBean = mutableListOf<TopBookDataBean>()
    fun add(list: List<TopBookDataBean>) {
        mListBean.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBookViewHolder = TopBookViewHolder(parent)

    override fun getItemCount(): Int = mListBean.size

    override fun onBindViewHolder(holder: TopBookViewHolder, position: Int) {
        holder.bind(mListBean[position])
    }
}

class TopBookViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val ivPic = itemView.findViewById<ImageView>(R.id.ivPic)
    private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
    private val tvLike = itemView.findViewById<TextView>(R.id.tvLike)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.tp_item_tp, parent, false))

    init {
        val screenWidth = tvTitle.context.getScreenWidth()
        val itemWidth = (screenWidth - tvTitle.context.dp2px(16) * 2 - tvTitle.context.dp2px(8)) / 2
        val itemHeight = itemWidth * 3 / 4
        ivPic.layoutParams.width = itemWidth
        ivPic.layoutParams.height = itemHeight
    }

    fun bind(bean: TopBookDataBean) {
        tvTitle.text = bean.title
        tvTime.text = bean.createTime
        tvLike.text = bean.likeTotal?.toString()
    }
}
