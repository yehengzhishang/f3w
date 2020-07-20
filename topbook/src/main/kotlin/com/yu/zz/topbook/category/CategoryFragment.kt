package com.yu.zz.topbook.category

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.bypass.createViewModel
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.topbook.R
import com.yu.zz.topbook.article.ArticleViewHolder
import com.yu.zz.topbook.article.articleClick
import com.yu.zz.topbook.employ.*
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.android.synthetic.main.topbook_fragment_category_single.*
import androidx.lifecycle.Observer as OB

const val KEY_CATEGORY_ID = "categoryId"
private const val LIMIT = 20
private const val SPAN_COUNT = 2

class CategorySingleFragment : TopBookFragment() {
    private val mRv: RecyclerView by lazy {
        requireView().findViewById<RecyclerView>(R.id.rv)
    }
    private val mViewModel: CategoryViewModel by lazy {
        DaggerCategoryComponent.builder().categoryModule(CategoryModule(this))
                .build().getViewModel()
    }

    private val mCategoryID: String by lazy { requireArguments().getString(KEY_CATEGORY_ID)!! }
    private val mAdapter: CategorySingleAdapter = CategorySingleAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topbook_fragment_category_single, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAdapter.settingClick(requireActivity())
        mRv.adapter = mAdapter
        mRv.layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
        val context = requireContext()
        mRv.addItemDecoration(TwoSpan(context.dp2px(DP_BORDER), context.dp2px(DP_MIDDLE), context.dp2px(DP_TOP)))
        mViewModel.dateNew.observe(viewLifecycleOwner, OB {
            srl.isRefreshing = false
            if (it == null || !it.isSuccess() || it.data == null) {
                cleanAdapter()
            }
            val result = it!!.data!!
            if (result.hasMore()) {
                srl.isEnabled = false
            }
            mAdapter.addBean(result.getList())
            mAdapter.notifyDataSetChanged()
        })
        mViewModel.requestArticleWithCategoryId(mCategoryID, start = mAdapter.itemCount, limit = LIMIT)
    }

    private fun cleanAdapter() {
        mAdapter.setBean(null)
        mAdapter.notifyDataSetChanged()
    }
}

class CategorySingleAdapter : RecyclerView.Adapter<ArticleViewHolder>() {
    private val mListBean = mutableListOf<ArticleTopBookBean>()
    private var itemClick: ((ArticleTopBookBean, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mListBean.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.click = itemClick
        holder.bind(bean = mListBean[position], position = position)
    }

    fun addBean(source: List<ArticleTopBookBean>) {
        mListBean.addAll(source)
    }

    fun setBean(source: List<ArticleTopBookBean>?) {
        mListBean.clear()
        val newSource = source ?: return
        addBean(newSource)
    }

    fun settingClick(activity: Activity) {
        itemClick = articleClick(activity)
    }
}

private class TwoSpan(private val pxBorder: Int, private val pxMiddle: Int, private val top: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        val left = position % 2 == 0
        outRect.top = top
        if (left) {
            outRect.left = pxBorder
            outRect.right = pxMiddle
        } else {
            outRect.right = pxBorder
            outRect.left = pxMiddle
        }
    }
}

@Module
class CategoryModule constructor(private val fragment: Fragment) {

    @Provides
    fun provideViewModel(factory: ViewModelProvider.Factory): CategoryViewModel {
        return createViewModel(fragment.viewModelStore, factory, CategoryViewModel::class.java)
    }

    @Provides
    fun provideFactory(): ViewModelProvider.Factory {
        return CategoryViewModelFactory(fragment.requireActivity().application, TopBookApi.INSTANCE.createService(TopBookService::class.java))
    }
}

@Component(modules = [CategoryModule::class])
interface CategoryComponent {
    fun getViewModel(): CategoryViewModel
}

class CategoryViewModelFactory constructor(private val app: Application, private val service: TopBookService) : ViewModelProvider.AndroidViewModelFactory(app) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(app, service) as T
        }
        return super.create(modelClass)
    }
}

class CategoryViewModel(app: Application, private val service: TopBookService) : TopBookViewModel(app) {
    private val mDataNet: MutableLiveData<ArticleResponseTopBookBean> by lazy {
        MutableLiveData<ArticleResponseTopBookBean>()
    }
    val dateNew: LiveData<ArticleResponseTopBookBean> get() = mDataNet

    fun requestArticleWithCategoryId(categoryId: String, start: Int, limit: Int) {
        service.getArticleWithCategoryId(categoryId, start = start.toString(), limit = limit.toString())
                .goToThreadMain()
                .subscribe(getNext { bean -> mDataNet.value = bean })
    }
}
