package com.yu.zz.topbook.category

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.rcd
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.topbook.R
import com.yu.zz.topbook.article.ArticleViewHolder
import com.yu.zz.topbook.article.articleClick
import com.yu.zz.topbook.employ.*
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val KEY_CATEGORY_ID = "categoryId"
private const val LIMIT = 20
private const val SPAN_COUNT = 2

@AndroidEntryPoint
class CategorySingleFragment : TopBookFragment() {
    private val mRv: RecyclerView by lazy {
        requireView().findViewById<RecyclerView>(R.id.rv)
    }
    private val srl: SwipeRefreshLayout by lazy {
        requireView().findViewById(R.id.srl)
    }

    private val mViewModel: CategoryViewModel by viewModels()

    private val mCategoryID: String by lazy { requireArguments().getString(KEY_CATEGORY_ID)!! }
    private val mAdapter: CategorySingleAdapter = CategorySingleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topbook_fragment_category_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.settingClick(requireActivity())
        mRv.adapter = mAdapter
        mRv.layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
        val context = requireContext()
        mRv.addItemDecoration(
            TwoSpan(
                context.dp2px(DP_BORDER),
                context.dp2px(DP_MIDDLE),
                context.dp2px(DP_TOP)
            )
        )

        mViewModel.dateNew.observe(viewLifecycleOwner) {
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
        }
        mViewModel.requestArticleWithCategoryId(
            mCategoryID,
            start = mAdapter.itemCount,
            limit = LIMIT
        )
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


@HiltViewModel
class CategoryViewModel @Inject constructor(private val repo: CategoryRepository) : ViewModel() {
    private val mDataNet: MutableLiveData<ArticleResponseTopBookBean> by lazy {
        MutableLiveData<ArticleResponseTopBookBean>()
    }
    val dateNew: LiveData<ArticleResponseTopBookBean> get() = mDataNet

    fun requestArticleWithCategoryId(categoryId: String, start: Int, limit: Int) {
        repo.getArticleWithCategoryId(categoryId, start.toString(), limit.toString())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<ArticleResponseTopBookBean> {
                var d: Disposable? = null
                override fun onSubscribe(d: Disposable) {
                    this.d = d
                    rcd.add(d)
                }

                override fun onNext(t: ArticleResponseTopBookBean) {
                    mDataNet.postValue(t)
                }

                override fun onError(e: Throwable) {
                    rcd.remove(d!!)
                }

                override fun onComplete() {
                    rcd.remove(d!!)
                }

            })
    }
}

class CategoryRepository @Inject constructor(private val service: TopBookService) {
    fun getArticleWithCategoryId(
        categoryId: String,
        start: String,
        limit: String
    ): Observable<ArticleResponseTopBookBean> {
        return service.getArticleWithCategoryId(categoryId, start, limit)
    }
}