package com.yu.zz.topbook.topic

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.topbook.databinding.TopbookTopicFragmentBinding
import com.yu.zz.topbook.employ.LoadScroller
import com.yu.zz.topbook.employ.TopBookFragment
import io.reactivex.Single


class TopicFragment : TopBookFragment() {

    private var mViewBinding: TopbookTopicFragmentBinding? = null
    private val mBinding get() = mViewBinding!!
    private val mViewModel: TopicViewModel by lazy {
        createViewModel(
            viewModelStore,
            TopicViewModelFactory(
                requireActivity().application,
                TopicRepository(createService(TopicService::class.java))
            ),
            TopicViewModel::class.java
        )
    }
    private val mAdapter: TopicAdapter = TopicAdapter().apply {
        this.clickBean = this@TopicFragment::goToDetail
    }
    private lateinit var mScroller: LoadScroller

    private val initRecyclerView: (rv: RecyclerView, adapter: RecyclerView.Adapter<*>) -> Unit =
        { rv, adapter ->
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
        ActivityCompat.startActivity(
            requireContext(),
            Intent(requireActivity(), DetailTopicActivity::class.java).apply {
                this.putExtra(KEY_ID, topicId.toString())
                putExtra("position_useless", position)
            },
            null
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel.dataList.observe(this, Observer {
            adaptList(it)
        })
        val loadMore = {
            mViewModel.loadList(mAdapter.itemCount)
        }
        mScroller = LoadScroller(loadMore)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = TopbookTopicFragmentBinding.inflate(inflater, container, false)
        initRecyclerView(mBinding.rv, mAdapter)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

class TopicViewModelFactory(private val app: Application, private val repo: ITopicRepository) :
    ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            return TopicViewModel(app, repo) as T
        }
        return super.create(modelClass)
    }
}


interface ITopicRepository {
    fun loadList(start: Int = 0, limit: Int = 10): Single<ResultTopicBean>
}

private class TopicRepository(private val service: TopicService) : ITopicRepository {
    override fun loadList(start: Int, limit: Int): Single<ResultTopicBean> {
        return service.requestList(start.toString(), limit.toString())
    }
}

