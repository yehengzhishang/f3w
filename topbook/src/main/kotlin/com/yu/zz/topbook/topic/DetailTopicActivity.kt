package com.yu.zz.topbook.topic

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.topbook.R
import com.yu.zz.topbook.employ.LoadScroller
import com.yu.zz.topbook.employ.TopBookActivity

const val KEY_ID = "topic_id"

class DetailTopicActivity : TopBookActivity() {
    private val mIdTopic by lazy {
        intent.getStringExtra(KEY_ID) ?: ""
    }
    private val mViewModel: DetailTopicViewModel by lazy {
        createViewModel<DetailTopicViewModel>(viewModelStore, DetailViewModelFactory(application, DetailRepository(createService())))
    }
    private val mRv: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rv)
    }
    private val mAdapter: ViewPointAdapter = ViewPointAdapter()
    private val loadMore: () -> Unit = {
        mViewModel.loadViewpoints(mIdTopic, mAdapter.itemCount)
    }
    private val mScroller: LoadScroller by lazy {
        return@lazy LoadScroller(loadMore)
    }

    private fun initRecyclerView(rv: RecyclerView) = rv.apply {
        layoutManager = LinearLayoutManager(this@DetailTopicActivity)
        adapter = mAdapter
        addOnScrollListener(mScroller)
    }

    private fun obNext(info: ViewpointInfo?) {
        mScroller.finishLoad()
        if (info == null) {
            return
        }
        if (info.isRefresh()) {
            mAdapter.clear()
        }
        mAdapter.add(info.list)
        mAdapter.notifyDataSetChanged()
    }

    override fun layoutId(): Int {
        return R.layout.topbook_topic_detail_activity
    }

    override fun createSecondUi() {
        initRecyclerView(mRv)
        mViewModel.dataViewPoints.observe(this, Observer { obNext(it) })
    }

    override fun createThirdData() {
        mViewModel.loadViewpoints(mIdTopic)
    }
}
