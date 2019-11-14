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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.common.arrange.goToThreadMain
import com.yu.zz.fwww.R
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
    private val mMapCategory = HashMap<String, CategoryTopBookBean>()
    private val mMapTp = HashMap<String, MutableList<ArticleTopBookBean>>()
    private val mListCategory = mutableListOf<CategoryTopBookBean>()
    private val mDataCategory by lazy { MutableLiveData<List<CategoryTopBookBean>>() }
    private val mDataTp by lazy { MutableLiveData<List<ArticleTopBookBean>>() }

    fun getPage(start: Int = 0, limit: Int = 20) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getPageConfig(start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<CategoryResponseTopBookBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: CategoryResponseTopBookBean) {
                        getItems(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    fun getDataCategory(): LiveData<List<CategoryTopBookBean>> {
        return mDataCategory
    }

    fun getDataTp(): LiveData<List<ArticleTopBookBean>> {
        return mDataTp
    }

    private fun getItems(topBookBean: CategoryResponseTopBookBean) {
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
                .subscribe(object : Observer<ArticleResponseTopBookBean> {
                    override fun onComplete() {
                        refreshData()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ArticleResponseTopBookBean) {
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
        val listAll = mutableListOf<ArticleTopBookBean>()
        for (category in mListCategory) {
            val list: MutableList<ArticleTopBookBean>? = mMapTp[category.categoryId!!.toString()]
                    ?: continue
            listAll.addAll(list!!)
        }
        mDataTp.postValue(listAll)
    }

    private fun addCategory(topBookBean: CategoryTopBookBean) {
        mMapCategory[topBookBean.categoryId!!.toString()] = topBookBean
        mListCategory.add(topBookBean)
    }


    private fun itemId(t: ArticleResponseTopBookBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(itemId: String, start: Int = 0, limit: Int = 8): Observable<ArticleResponseTopBookBean> {
        return TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getTopBookList(itemId, start.toString(), limit.toString())
    }
}

class TopBookAdapter : RecyclerView.Adapter<TopBookViewHolder>() {
    private val mListBean = mutableListOf<ArticleTopBookBean>()
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

    fun add(list: List<ArticleTopBookBean>) {
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

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.topbook_item_article, parent, false))

    fun bind(topBookBean: ArticleTopBookBean) {
        Glide.with(ivPic).load(topBookBean.cover).into(ivPic)
        tvTitle.text = topBookBean.title
        tvTime.text = topBookBean.createTime
        tvLike.text = topBookBean.likeTotal?.toString()
    }
}

class TitleTopBookViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitleName = itemView.findViewById<TextView>(R.id.tvName)
    private val tvMore = itemView.findViewById<TextView>(R.id.btnMore)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.item_topbook_title, parent, false))

    fun bind(categoryBean: CategoryTopBookBean) {
        tvTitleName.text = categoryBean.name
        tvMore.setOnClickListener {
            Snackbar.make(tvMore, "都闪开！我要跳转了！！！", Snackbar.LENGTH_SHORT).show()
        }
    }
}
