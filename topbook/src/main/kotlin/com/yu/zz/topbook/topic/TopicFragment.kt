package com.yu.zz.topbook.topic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.topbook.databinding.TopbookTopicFragmentBinding
import com.yu.zz.topbook.employ.TopBookFragment


class TopicFragment : TopBookFragment() {
    private var mViewBinding: TopbookTopicFragmentBinding? = null
    private val mBinding get() = mViewBinding!!
    private val mAdapter: TopicAdapter = TopicAdapter()
    private lateinit var mViewModel: TopicViewModel

    private val initRecyclerView: (rv: RecyclerView, adapter: RecyclerView.Adapter<*>) -> Unit = { rv, adapter ->
        rv.layoutManager = LinearLayoutManager(rv.context)
        rv.adapter = adapter
    }

    private val adaptList: (ListInfo?) -> Unit = adapter@{ listInfo ->
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = createViewModel()
        mViewModel.dataList.observe(this, Observer {
            adaptList(it)
        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        mViewBinding = null
    }
}
