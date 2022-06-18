package com.yu.zz.topbook.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yu.zz.bypass.RxCompositeDisposable
import com.yu.zz.common.arrange.createViewModel
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.topbook.R
import com.yu.zz.topbook.category.CategorySingleAdapter
import com.yu.zz.topbook.category.CategorySingleFragment
import com.yu.zz.topbook.category.KEY_CATEGORY_ID
import com.yu.zz.topbook.databinding.SearchActivityBinding
import com.yu.zz.topbook.databinding.TopbookFragmentCategorySingleBinding
import com.yu.zz.topbook.employ.*
import com.yu.zz.topbook.load.ListUseCase
import com.yu.zz.topbook.load.LoadRequestBean
import com.yu.zz.topbook.load.LoadUseCase
import io.reactivex.Single

class SearchActivity : AppCompatActivity(), KeywordProvider {

    private lateinit var mBinding: SearchActivityBinding
    private var mediator: TabLayoutMediator? = null
    private val mViewModel: SearchViewModel by lazy {
        createViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        mBinding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val vp: ViewPager2 = findViewById<ViewPager2>(R.id.vp).apply { };
        vp.adapter = SearchPageAdapter(this, listOf(ArticleFragment(), ArticleFragment()))
        mViewModel.livedataTab.observe(this, this::obTabs)
    }

    private fun obTabs(tabs: List<String>) {
        mediator?.detach()
        mediator = TabLayoutMediator(findViewById(R.id.tl), findViewById(R.id.vp)) { tab, pos ->
            tab.tag = pos
            tab.text = tabs[pos]
        }.apply {
            attach()
        }
    }

    override fun getKeyword(): String {
        return "时间"
    }
}

class ArticleFragment : Fragment() {

    private lateinit var mBinding: TopbookFragmentCategorySingleBinding

    private val mRv: RecyclerView get() = mBinding.rv

    private val mSrl: SwipeRefreshLayout = mBinding.srl

    private val mViewModel: ArticleViewModel by lazy {
        createViewModel()
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return super.getDefaultViewModelProviderFactory()
    }

    private val mAdapter: CategorySingleAdapter = CategorySingleAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TopbookFragmentCategorySingleBinding.inflate(inflater, container, false)
        mSrl.isEnabled = false
        mRv.adapter = mAdapter
        val context = requireContext();
        mRv.layoutManager = GridLayoutManager(context, 2)

        mRv.addItemDecoration(
            TwoSpan(
                context.dp2px(DP_BORDER),
                context.dp2px(DP_MIDDLE),
                context.dp2px(DP_TOP)
            )
        )
        return mBinding.root
    }

}

class ArticleViewModel constructor(
    private val loadUseCase: LoadUseCase<LoadRequestBean<String>, Single<TopBookBean<ListTopBookBean<ArticleTopBookBean>>>>,
    private val listUseCase: ListUseCase<ArticleTopBookBean>
) : ViewModel() {

    private val mRcd = RxCompositeDisposable()
    private val mLiveData: MutableLiveData<List<ArticleTopBookBean>> = MutableLiveData()
    val listData: LiveData<List<ArticleTopBookBean>> get() = mLiveData

    private fun request(keyword: String, start: Int, limit: Int) {

    }

    fun refresh() {

    }

    fun loadMore() {

    }

    override fun onCleared() {
        super.onCleared()
        mRcd.dispose()
    }
}

private class TwoSpan(private val pxBorder: Int, private val pxMiddle: Int, private val top: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
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

class SearchPageAdapter(activity: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = CategorySingleFragment().apply {
        arguments = Bundle().apply {
            putString(KEY_CATEGORY_ID, "24")
        }
    }
}

class SearchViewModel constructor() : ViewModel() {
    private val mLivedata: MutableLiveData<List<String>> = MutableLiveData(listOf("文章", "话题"))
    val livedataTab: LiveData<List<String>> get() = mLivedata
}


interface KeywordProvider {
    fun getKeyword(): String
}

