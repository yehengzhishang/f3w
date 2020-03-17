package com.yu.zz.topbook.category

import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.common.arrange.dp2px
import com.yu.zz.common.arrange.goToThreadMain
import com.yu.zz.topbook.ArticleTopBookViewHolder
import com.yu.zz.topbook.R
import com.yu.zz.topbook.deep.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.topbook_fragment_category_single.*
import androidx.lifecycle.Observer as OB

const val KEY_CATEGORY_ID = "categoryId"
private const val LIMIT = 20
private const val SPAN_COUNT = 2

class CategorySingleFragment : Fragment() {
    private val mRv: RecyclerView by lazy {
        view!!.findViewById<RecyclerView>(R.id.rv)
    }
    private val mViewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this, CategoryFactory(app = activity!!.application)).get(CategoryViewModel::class.java)
    }

    private val mCategoryID: String by lazy { arguments!!.getString(KEY_CATEGORY_ID)!! }
    private val mAdapter: CategorySingleAdapter = CategorySingleAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topbook_fragment_category_single, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRv.adapter = mAdapter
        mRv.layoutManager = GridLayoutManager(activity!!, SPAN_COUNT)
        val context = context!!
        mRv.addItemDecoration(TwoSpan(context.dp2px(DP_BORDER), context.dp2px(DP_MIDDLE), context.dp2px(DP_TOP)))
        mViewModel.getDataNew().observe(this, OB {
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

class CategorySingleAdapter : RecyclerView.Adapter<ArticleTopBookViewHolder>() {
    private val mListBean = mutableListOf<ArticleTopBookBean>()

    fun addBean(source: List<ArticleTopBookBean>) {
        mListBean.addAll(source)
    }

    fun setBean(source: List<ArticleTopBookBean>?) {
        mListBean.clear()
        val newSource = source ?: return
        addBean(newSource)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleTopBookViewHolder {
        return ArticleTopBookViewHolder(parent)
    }

    override fun getItemCount(): Int = mListBean.size

    override fun onBindViewHolder(holder: ArticleTopBookViewHolder, position: Int) {
        holder.bind(bean = mListBean[position], position = position)
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

class CategoryViewModel(app: Application) : AndroidViewModel(app) {
    private val mDataNet: MutableLiveData<ArticleResponseTopBookBean> by lazy {
        MutableLiveData<ArticleResponseTopBookBean>()
    }

    fun getDataNew(): LiveData<ArticleResponseTopBookBean> {
        return mDataNet
    }

    fun requestArticleWithCategoryId(categoryId: String, start: Int, limit: Int) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getArticleWithCategoryId(categoryId, start = start.toString(), limit = limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<ArticleResponseTopBookBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ArticleResponseTopBookBean) {
                        mDataNet.value = t
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }
}

class CategoryFactory(private val app: Application) : ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass::class.java.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(app) as T
        }
        return super.create(modelClass)
    }
}