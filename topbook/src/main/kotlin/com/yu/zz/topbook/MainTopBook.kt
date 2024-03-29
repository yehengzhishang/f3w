package com.yu.zz.topbook

import android.app.Application
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yu.zz.bypass.createViewModel
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.bypass.observeOnce
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.common.arrange.toast
import com.yu.zz.topbook.article.ArticleViewHolder
import com.yu.zz.topbook.category.CategoryActivity
import com.yu.zz.topbook.category.KEY_ID_CATEGORY
import com.yu.zz.topbook.databinding.TopbookActivityMainBinding
import com.yu.zz.topbook.employ.*
import com.yu.zz.topbook.search.SearchActivity
import com.yu.zz.topbook.topic.TopicFragment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class MainViewModel(app: Application, private val repo: MainRepository) : TopBookViewModel(app) {
    private val mMapCategory = HashMap<String, CategoryTopBookBean>()
    private val mMapTp = HashMap<String, MutableList<ArticleTopBookBean>>()
    private val mListCategory = mutableListOf<CategoryTopBookBean>()

    fun load(start: Int = 0, limit: Int = 20): LiveData<List<Any>> {
        val dataList = MutableLiveData<List<Any>>()
        repo.requestAllCategory(start.toString(), limit.toString())
            .flatMap { Observable.fromIterable(it) }
            .filter { it.categoryId != null }
            .doOnNext { addCategory(it!!) }
            .map { it.categoryId!! }
            .flatMap { getObsItem(it.toString()) }
            .goToThreadMain()
            .subscribe(getNextComplete(next = this::next, complete = {
                updateData(dataList)
            }))
        return dataList
    }

    private fun next(bean: ArticleResponseTopBookBean) {
        val itemId = itemId(bean) ?: return
        if (!mMapCategory.keys.contains(itemId)) {
            return
        }
        val list = mMapTp.getOrPut(itemId, { mutableListOf() })
        val listSource = bean.data?.items!!
        for (sourceBean in listSource) {
            if (sourceBean == null) {
                continue
            }
            list.add(sourceBean)
        }
    }

    private fun updateData(dataList: MutableLiveData<List<Any>>) {
        mListCategory.sortByDescending { it.categoryId }
        val listAll = mutableListOf<Any>()
        for (category in mListCategory) {
            val list: MutableList<ArticleTopBookBean> = mMapTp[category.categoryId!!.toString()]
                ?: continue
            listAll.add(category)
            listAll.addAll(list)
        }
        dataList.value = listAll
    }

    private fun addCategory(topBookBean: CategoryTopBookBean) {
        mMapCategory[topBookBean.categoryId!!.toString()] = topBookBean
        mListCategory.add(topBookBean)
    }

    private fun itemId(t: ArticleResponseTopBookBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(
        itemId: String,
        start: Int = 0,
        limit: Int = 4
    ): Observable<ArticleResponseTopBookBean> {
        return repo.getArticleWithCategoryId(itemId, start.toString(), limit.toString())
            .subscribeOn(Schedulers.io())
    }
}

class MainRepository constructor(private val service: TopBookService) {
    fun requestAllCategory(
        start: String,
        limit: String
    ): Observable<MutableList<CategoryTopBookBean?>> {
        return service.getListCategory(start, limit)
            .filter { it.isSuccess() && it.data != null }
            .map { it.data!! }
            .filter { it.items != null }
            .map { it.items!! }

    }

    fun getArticleWithCategoryId(
        itemId: String,
        start: String,
        limit: String
    ): Observable<ArticleResponseTopBookBean> {
        return service.getArticleWithCategoryId(itemId, start, limit)
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
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
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
            VIEW_TYPE_A -> ArticleViewHolder(parent)
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

class CategoryTopBookViewHolder private constructor(parent: ViewGroup, layoutId: Int) :
    TopBookViewHolder<CategoryTopBookBean>(parent, layoutId) {
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

class MainTopBookFragment : TopBookFragment() {
    private var _viewBinding: TopbookActivityMainBinding? = null
    private val mViewBinding get() = _viewBinding!!
    private val mAdapter = TopBookAdapter().apply {
        this.click = click@{ bean, _ ->
            if (bean == null) {
                return@click
            }
            when (bean) {
                is CategoryTopBookBean -> skip(bean)
                is ArticleTopBookBean -> Snackbar.make(
                    mViewBinding.rv,
                    "文章详情页面正在生成中",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
    private val mViewModel by lazy {
        createViewModel<MainViewModel>(
            viewModelStore,
            MainViewModelFactory(
                requireActivity().application,
                MainRepository(createService(TopBookService::class.java))
            )
        )
    }


    private fun skip(bean: CategoryTopBookBean) {
        startActivity(Intent(requireContext(), CategoryActivity::class.java).apply {
            putExtra(KEY_ID_CATEGORY, bean)
        })
    }

    private fun createSecondUi() {
        mViewBinding.rv.run {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }
    }

    private fun createThirdData() {
        mViewModel.load().observeOnce(this) OB@{ list ->
            if (list == null) {
                return@OB
            }
            mViewBinding.srl.isRefreshing = false
            mViewBinding.srl.isEnabled = false
            mAdapter.add(list)
        }
        mViewBinding.srl.isRefreshing = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = TopbookActivityMainBinding.inflate(inflater, container, false)
        createSecondUi()
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createThirdData()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "TopBook"
    }

}

class MainViewModelFactory(private val app: Application, private val repo: MainRepository) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app, repo) as T
        }
        return super.create(modelClass)
    }

}

class FoundationTopBookActivity : TopBookActivity() {
    private var mTimeBack = 0L
    private var mFragmentCurrent: TopBookFragment? = null
    private val mFragmentMain: MainTopBookFragment by lazy {
        MainTopBookFragment()
    }
    private val mFragmentTopic: TopicFragment by lazy {
        TopicFragment()
    }

    private val topChange: () -> Boolean = {
        if (mFragmentCurrent != mFragmentTopic) {
            changeFragment(mFragmentTopic)
        } else {
            changeFragment(mFragmentMain)
        }
        true
    }

    override fun layoutId(): Int {
        return R.layout.topbook_activity_foundation
    }

    override fun createSecondUi() {
        changeFragment(mFragmentMain)
    }

    override fun createThirdData() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topboob_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change -> changeAssist()
            R.id.action_state -> topChange()
            R.id.action_search -> goSearch()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val current = System.currentTimeMillis()
        if (current - mTimeBack > 1000) {
            mTimeBack = current
            toast("再按一次退出")
            return
        }
        super.onBackPressed()
    }

    private fun changeAssist(): Boolean {
        startActivity(Intent(this, AssistTopBookActivity::class.java))
        finish()
        return true
    }

    private fun goSearch(): Boolean {
//        Snackbar.make(findViewById(R.id.fcv_tp), "搜索", Snackbar.LENGTH_SHORT).show()
        ActivityCompat.startActivity(this, Intent(this, SearchActivity::class.java), null)
        return true
    }

    private fun changeFragment(fragmentTarget: TopBookFragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        mFragmentCurrent?.let { current ->
            beginTransaction.hide(current)
        }
        if (!fragmentTarget.isAdded) {
            beginTransaction.run {
                add(R.id.fcv_tp, fragmentTarget)
            }
        } else {
            beginTransaction.run {
                show(fragmentTarget)
            }
        }
        beginTransaction.commitAllowingStateLoss()
        mFragmentCurrent = fragmentTarget
    }
}
