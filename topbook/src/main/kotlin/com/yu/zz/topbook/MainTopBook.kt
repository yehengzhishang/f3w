package com.yu.zz.topbook

import android.app.Application
import android.content.Intent
import android.graphics.Rect
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.common.arrange.goToThreadMain
import com.yu.zz.common.base.createViewModel
import com.yu.zz.topbook.category.CategoryActivity
import com.yu.zz.topbook.category.KEY_ID_CATEGORY
import com.yu.zz.topbook.deep.*
import com.yu.zz.topbook.deep.employ.TopBookActivity
import com.yu.zz.topbook.deep.employ.TopBookViewModel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.topbook_activity_main.*
import pl.droidsonroids.gif.GifImageView
import androidx.lifecycle.Observer as OB

class MainTopBookActivity : TopBookActivity() {
    private val mAdapter = TopBookAdapter().apply {
        this.click = click@{ bean, _ ->
            if (bean == null) {
                return@click
            }
            when (bean) {
                is CategoryTopBookBean -> skip(bean)
                is ArticleTopBookBean -> Snackbar.make(rv, "文章详情页面正在生成中", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private val mViewModel by lazy {
        createViewModel(this, MainViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.topbook_activity_main
    }

    override fun createSecondUi() {
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = mAdapter
        mViewModel.getDataList().observe(this, OB {
            if (it == null) {
                return@OB
            }
            srl.isRefreshing = false
            srl.isEnabled = false
            mAdapter.add(it)
        })
    }

    override fun createThirdData() {
        mViewModel.getPage()
        srl.isRefreshing = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topboob_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change -> changeAssist()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun skip(bean: CategoryTopBookBean) {
        startActivity(Intent(this, CategoryActivity::class.java).apply {
            putExtra(KEY_ID_CATEGORY, bean)
        })
    }

    private fun changeAssist(): Boolean {
        startActivity(Intent(this, AssistTopBookActivity::class.java))
        finish()
        return true
    }
}

class MainViewModel(app: Application) : TopBookViewModel(app) {
    private val mMapCategory = HashMap<String, CategoryTopBookBean>()
    private val mMapTp = HashMap<String, MutableList<ArticleTopBookBean>>()
    private val mListCategory = mutableListOf<CategoryTopBookBean>()
    private val mDataCategory by lazy { MutableLiveData<List<CategoryTopBookBean>>() }
    private val mDataList by lazy { MutableLiveData<List<Any>>() }

    fun getPage(start: Int = 0, limit: Int = 20) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getListCategory(start.toString(), limit.toString())
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
                .subscribe(getNextComplete(next = next@{ bean ->
                    val itemId = itemId(bean) ?: return@next
                    if (!mMapCategory.keys.contains(itemId)) {
                        return@next
                    }
                    val list = mMapTp.getOrPut(itemId, { mutableListOf() })
                    val listSource = bean.data?.items!!
                    for (sourceBean in listSource) {
                        if (sourceBean == null) {
                            continue
                        }
                        list.add(sourceBean)
                    }

                }, complete = {
                    refreshData()
                }))
    }

    fun getDataList(): LiveData<List<Any>> {
        return mDataList
    }

    private fun refreshData() {
        mListCategory.sortByDescending { it.categoryId }
        mDataCategory.postValue(mListCategory)
        val listAll = mutableListOf<Any>()
        for (category in mListCategory) {
            val list: MutableList<ArticleTopBookBean> = mMapTp[category.categoryId!!.toString()]
                    ?: continue
            listAll.add(category)
            listAll.addAll(list)
        }
        mDataList.postValue(listAll)
    }

    private fun addCategory(topBookBean: CategoryTopBookBean) {
        mMapCategory[topBookBean.categoryId!!.toString()] = topBookBean
        mListCategory.add(topBookBean)
    }


    private fun itemId(t: ArticleResponseTopBookBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(itemId: String, start: Int = 0, limit: Int = 4): Observable<ArticleResponseTopBookBean> {
        return TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getArticleWithCategoryId(itemId, start.toString(), limit.toString())
    }
}

private const val VIEW_TYPE_A = 1
private const val VIEW_TYPE_P = 2

class TopBookAdapter : RecyclerView.Adapter<TopBookViewHolder<*>>() {
    private val mListBean = mutableListOf<Any>()
    private val mMapPosition = HashMap<Any, Int>()
    var click: ((bean: Any?, position: Int) -> Unit)? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = getItemViewType(position)
        }
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildLayoutPosition(view)
                if (mListBean[position] is CategoryTopBookBean) {
                    return
                }
                outRect.top = view.context.dp2px(8)
                val leftPosition = mMapPosition[mListBean[position]] ?: return
                if (leftPosition % 2 == 0) {
                    outRect.left = view.context.dp2px(12)
                    outRect.right = view.context.dp2px(4)
                } else {
                    outRect.left = view.context.dp2px(4)
                    outRect.right = view.context.dp2px(12)
                }
            }
        })
    }

    fun add(list: List<Any>) {
        mListBean.addAll(list)
        var type = -1
        for (an in mListBean) {
            if (an is CategoryTopBookBean) {
                type = -1
                continue
            }
            type++
            mMapPosition[an] = type % 2
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBookViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_A -> ArticleTopBookViewHolder(parent)
            VIEW_TYPE_P -> CategoryTopBookViewHolder(parent)
            else -> throw RuntimeException("cant find view Type")
        }
    }

    override fun getItemCount(): Int = mListBean.size

    override fun onBindViewHolder(holder: TopBookViewHolder<*>, position: Int) {
        holder.click = this.click
        holder.bindAny(mListBean[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mListBean[position] is ArticleTopBookBean -> VIEW_TYPE_A
            mListBean[position] is CategoryTopBookBean -> VIEW_TYPE_P
            else -> throw RuntimeException("cant find type")
        }
    }
}


class ArticleTopBookViewHolder private constructor(parent: ViewGroup, layoutId: Int) : TopBookViewHolder<ArticleTopBookBean>(parent, layoutId) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val ivPic = itemView.findViewById<GifImageView>(R.id.ivPic)
    private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
    private val tvLike = itemView.findViewById<TextView>(R.id.tvLike)

    constructor(parent: ViewGroup) : this(parent, R.layout.topbook_item_article)

    override fun bind(bean: ArticleTopBookBean, position: Int) {
        itemView.setOnClickListener {
            click?.invoke(bean, position)
        }
        Glide.with(ivPic).load(bean.cover).into(ivPic)
        tvTitle.text = bean.title
        tvTime.text = bean.createTime
        tvLike.text = bean.likeTotal?.toString()
    }
}

class CategoryTopBookViewHolder private constructor(parent: ViewGroup, layoutId: Int) : TopBookViewHolder<CategoryTopBookBean>(parent, layoutId) {
    private val tvTitleName = itemView.findViewById<TextView>(R.id.tvName)
    private val tvMore = itemView.findViewById<TextView>(R.id.btnMore)

    constructor(parent: ViewGroup) : this(parent, R.layout.topbook_item_title)

    override fun bind(bean: CategoryTopBookBean, position: Int) {
        tvTitleName.text = bean.name
        tvMore.setOnClickListener {
            click?.invoke(bean, position)
        }
    }
}
