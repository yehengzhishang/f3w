package com.yu.zz.topbook.topic

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.topbook.R
import com.yu.zz.topbook.employ.TopBookActivity

const val KEY_ID = "topic_id"

class DetailTopicActivity : TopBookActivity() {
    private val mIdTopic by lazy {
        intent.getStringExtra(KEY_ID) ?: ""
    }
    private val mViewModel: DetailTopicViewModel by lazy {
        createViewModel(DetailTopicViewModel::class.java)
    }
    private val mRv: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rv)
    }
    private val mAdapter: ViewPointAdapter = ViewPointAdapter()
    private val mScroller: ViewpointScroller by lazy {
        return@lazy ViewpointScroller(mIdTopic, mViewModel)
    }

    private fun initRecyclerView(rv: RecyclerView) {
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mAdapter
        rv.addOnScrollListener(mScroller)
        mAdapter.notifyDataSetChanged()
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

private class ViewpointScroller(private val topicId: String, private val viewModel: DetailTopicViewModel) : RecyclerView.OnScrollListener() {
    private var mIsLoad = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (mIsLoad) {
            return
        }
        if (dy <= 0) {
            return
        }
        val layoutManager = recyclerView.layoutManager ?: return
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val adapter = recyclerView.adapter ?: return
        val firstPosition = linearLayoutManager.findLastVisibleItemPosition()
        if (firstPosition > adapter.itemCount - 5) {
            mIsLoad = true
            viewModel.loadViewpoints(topicId, adapter.itemCount)
        }
    }

    fun finishLoad() {
        mIsLoad = false
    }
}