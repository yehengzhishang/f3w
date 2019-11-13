package com.yu.zz.tb

import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.fwww.R
import com.yu.zz.tb.arrange.goToThreadMain
import com.yu.zz.tb.deep.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_topbook_main.*
import pl.droidsonroids.gif.GifImageView
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
        mViewModel.getPage()
    }
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val mMapCategory = HashMap<String, PageTopBookBean>()
    private val mMapTp = HashMap<String, MutableList<DataTopBookBean>>()
    private val mListCategory = mutableListOf<PageTopBookBean>()
    private val mDataCategory by lazy { MutableLiveData<List<PageTopBookBean>>() }
    private val mDataTp by lazy { MutableLiveData<List<DataTopBookBean>>() }

    fun getPage(start: Int = 0, limit: Int = 20) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getPageConfig(start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<PageResponseTopBookBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: PageResponseTopBookBean) {
                        getItems(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    fun getDataCategory(): LiveData<List<PageTopBookBean>> {
        return mDataCategory
    }

    fun getDataTp(): LiveData<List<DataTopBookBean>> {
        return mDataTp
    }

    private fun getItems(topBookBean: PageResponseTopBookBean) {
        Observable.just(topBookBean)
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
                .subscribe(object : Observer<ResponseTopBookBean> {
                    override fun onComplete() {
                        refreshData()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseTopBookBean) {
                        val itemId = itemId(t) ?: return
                        if (!mMapCategory.keys.contains(itemId)) {
                            return
                        }
                        val list = mMapTp.getOrPut(itemId, { mutableListOf() })
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
        val listAll = mutableListOf<DataTopBookBean>()
        for (category in mListCategory) {
            val list: MutableList<DataTopBookBean>? = mMapTp[category.categoryId!!.toString()]
                    ?: continue
            listAll.addAll(list!!)
        }
        mDataTp.postValue(listAll)
    }

    private fun addCategory(topBookBean: PageTopBookBean) {
        mMapCategory[topBookBean.categoryId!!.toString()] = topBookBean
        mListCategory.add(topBookBean)
    }


    private fun itemId(t: ResponseTopBookBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(itemId: String, start: Int = 0, limit: Int = 8): Observable<ResponseTopBookBean> {
        return TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getTopBookList(itemId, start.toString(), limit.toString())
    }
}

class TopBookAdapter : RecyclerView.Adapter<TopBookViewHolder>() {
    private val mListBean = mutableListOf<DataTopBookBean>()
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildLayoutPosition(view)
                outRect.top = view.context.dp2px(8)
                if (position % 2 == 0) {
                    outRect.left = view.context.dp2px(12)
                    outRect.right = view.context.dp2px(4)
                } else {
                    outRect.left = view.context.dp2px(4)
                    outRect.right = view.context.dp2px(12)
                }
            }
        })
    }

    fun add(list: List<DataTopBookBean>) {
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
    private val ivPic = itemView.findViewById<GifImageView>(R.id.ivPic)
    private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
    private val tvLike = itemView.findViewById<TextView>(R.id.tvLike)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.tp_item_tp, parent, false))

    fun bind(topBookBean: DataTopBookBean) {
        Glide.with(ivPic).load(topBookBean.cover).into(ivPic)
        tvTitle.text = topBookBean.title
        tvTime.text = topBookBean.createTime
        tvLike.text = topBookBean.likeTotal?.toString()
    }
}
