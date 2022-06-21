package com.yu.zz.topbook.search

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yu.zz.bypass.RxCompositeDisposable
import com.yu.zz.bypass.add
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.arrange.createViewModel
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.topbook.R
import com.yu.zz.topbook.category.CategorySingleAdapter
import com.yu.zz.topbook.databinding.SearchActivityBinding
import com.yu.zz.topbook.databinding.TopbookFragmentCategorySingleBinding
import com.yu.zz.topbook.employ.*
import com.yu.zz.topbook.load.AnswerUseCase
import com.yu.zz.topbook.load.ListRequestBean
import com.yu.zz.topbook.load.ListUseCase
import com.yu.zz.topbook.load.ListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject

@AndroidEntryPoint
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
        vp.adapter = SearchPageAdapter(this, listOf(ArticleFragment(), TopicFragment()))
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

@AndroidEntryPoint
open class ArticleFragment : Fragment() {

    private lateinit var mBinding: TopbookFragmentCategorySingleBinding

    private val mRv: RecyclerView by lazy { mBinding.rv }

    private val mSrl: SwipeRefreshLayout by lazy { mBinding.srl }

    private val mViewModel: ArticleViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.listData.observe(this.viewLifecycleOwner, Observer {
            mAdapter.addBean(it)
            mAdapter.notifyItemRangeChanged(0, it.size)
        })
        mViewModel.request()
    }

}

@Module
@InstallIn(ViewModelComponent::class)
class ArticleModule {
    @Provides
    fun provideListUseCase(): ListUseCase<ArticleTopBookBean> {
        return ListUseCaseImpl()
    }

    @Provides
    fun provideService(): TopBookService {
        return TopBookApi.INSTANCE.createService(TopBookService::class.java)
    }

}

class ArticleLoadUseCase @Inject constructor(private val service: TopBookService) :
    AnswerUseCase<ListRequestBean<String>, Observable<ArticleResponseTopBookBean>> {
    override fun ask(word: ListRequestBean<String>): Observable<ArticleResponseTopBookBean> {
        return service.getArticleWithCategoryId(
            word.keyword,
            word.start.toString(),
            word.limit.toString()
        )
    }
}


@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val listUseCase: ListUseCase<ArticleTopBookBean>,
    private val answerUseCase: ArticleLoadUseCase
) : ViewModel() {

    private val mRcd = RxCompositeDisposable()
    private val mLiveData: MutableLiveData<List<ArticleTopBookBean>> = MutableLiveData()
    val listData: LiveData<List<ArticleTopBookBean>> get() = mLiveData

    fun request() {
        answerUseCase.ask(ListRequestBean("24", 0, 20))
            .goToThreadMain()
            .subscribe(
                { mLiveData.value = listUseCase.stretch(it.data!!.getList()) },
                {
                    Log.e("rainrain", it.message!!)
                }
            ).add(mRcd)
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

    override fun createFragment(position: Int): Fragment = fragments[position]
}

class SearchViewModel constructor() : ViewModel() {
    private val mLivedata: MutableLiveData<List<String>> = MutableLiveData(listOf("文章", "话题"))
    val livedataTab: LiveData<List<String>> get() = mLivedata
}


interface KeywordProvider {
    fun getKeyword(): String
}

