package com.yu.zz.topbook.topic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.topbook.databinding.TopbookTopicFragmentBinding
import com.yu.zz.topbook.employ.LoadScroller
import com.yu.zz.topbook.employ.TopBookFragment


class TopicFragment : TopBookFragment() {
    private var mViewBinding: TopbookTopicFragmentBinding? = null
    private val mBinding get() = mViewBinding!!
    private val mAdapter: TopicAdapter = TopicAdapter().apply {
        this.clickBean = this@TopicFragment::goToDetail
    }
    private lateinit var mViewModel: TopicViewModel
    private lateinit var mScroller: LoadScroller

    private val initRecyclerView: (rv: RecyclerView, adapter: RecyclerView.Adapter<*>) -> Unit = { rv, adapter ->
        rv.layoutManager = LinearLayoutManager(rv.context)
        rv.adapter = adapter
        rv.addOnScrollListener(mScroller)
    }

    private val adaptList: (ListInfo?) -> Unit = adapter@{ listInfo ->
        mScroller.finishLoad()
        val info = listInfo ?: return@adapter
        if (info.isNeedClear()) {
            mAdapter.clearList()
        }
        val list = info.list
        if (!list.isNullOrEmpty()) {
            mAdapter.addList(list)
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun goToDetail(bean: TopicBean, position: Int) {
        val topicId = bean.topicId ?: return
        ActivityCompat.startActivity(requireContext(), Intent(requireActivity(), DetailTopicActivity::class.java).apply {
            this.putExtra(KEY_ID, topicId.toString())
        }, null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = createViewModel()
        mViewModel.dataList.observe(this, Observer {
            adaptList(it)
        })
        val loadMore = {
            mViewModel.loadList(mAdapter.itemCount)
        }
        mScroller = LoadScroller(loadMore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewBinding = TopbookTopicFragmentBinding.inflate(inflater, container, false)
        initRecyclerView(mBinding.rv, mAdapter)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.loadList()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "Topic"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewBinding = null
    }
}

