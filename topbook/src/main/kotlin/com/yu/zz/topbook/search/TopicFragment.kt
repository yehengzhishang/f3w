package com.yu.zz.topbook.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yu.zz.bypass.RxCompositeDisposable
import com.yu.zz.bypass.add
import com.yu.zz.bypass.goToThreadIO
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.topbook.databinding.TopbookFragmentCategorySingleBinding
import com.yu.zz.topbook.employ.ListTopBookBean
import com.yu.zz.topbook.employ.SingleTopBookService
import com.yu.zz.topbook.employ.TopBookBean
import com.yu.zz.topbook.load.AnswerUseCase
import com.yu.zz.topbook.load.ListRequestBean
import com.yu.zz.topbook.load.ListUseCase
import com.yu.zz.topbook.load.ListUseCaseImpl
import com.yu.zz.topbook.topic.TopicAdapter
import com.yu.zz.topbook.topic.TopicBean
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableSingleObserver
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class TopicFragment : Fragment() {

    private val mViewModel: TopicViewModel by viewModels()

    private lateinit var mBinding: TopbookFragmentCategorySingleBinding

    private val mRv: RecyclerView by lazy { mBinding.rv }

    private val mSrl: SwipeRefreshLayout by lazy { mBinding.srl }

    private val mAdapter: TopicAdapter = TopicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TopbookFragmentCategorySingleBinding.inflate(inflater, container, false)
        mSrl.setOnRefreshListener {
            Single.timer(1, TimeUnit.SECONDS).goToThreadMain()
                .subscribe(Consumer<Long> {
                    val list: MutableList<TopicBean> = mViewModel.livedata.value!!.toMutableList()
                    list.removeFirst()
                    list.removeFirst()
                    mAdapter.clearList()
                    mAdapter.addList(list)
                    mAdapter.notifyItemRangeRemoved(0,2)
                    mSrl.isRefreshing = false
                })
        }
        mRv.adapter = mAdapter
        val context = requireContext()
        mRv.layoutManager = LinearLayoutManager(context)
        mRv.itemAnimator = DefaultItemAnimator().apply {
            addDuration = 4000
            removeDuration = 2000
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.livedata.observe(this.viewLifecycleOwner) {
            mAdapter.addList(it)
            mAdapter.notifyItemRangeRemoved(0, 2)
        }
        mViewModel.request("时间")
    }

}

@Module
@InstallIn(ViewModelComponent::class)
object TopicModule {
    @Provides
    fun provideUseCase(): ListUseCase<TopicBean> {
        return ListUseCaseImpl()
    }
}

class TopicLoadUseCase @Inject constructor(private val service: SingleTopBookService) :
    AnswerUseCase<ListRequestBean<String>, Single<TopBookBean<ListTopBookBean<TopicBean>>>> {
    override fun ask(word: ListRequestBean<String>): Single<TopBookBean<ListTopBookBean<TopicBean>>> {
        return service.searchTopic(word.keyword, word.start.toString(), word.limit.toString())
    }
}

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val answerUseCase: TopicLoadUseCase,
    private val listUseCase: ListUseCase<TopicBean>
) : ViewModel() {
    private val mLivedata: MutableLiveData<List<TopicBean>> = MutableLiveData()
    val livedata: LiveData<List<TopicBean>> get() = mLivedata
    private val mRcd = RxCompositeDisposable()

    fun request(keyword: String) {
        answerUseCase.ask(ListRequestBean(keyword, 0, 20))
            .goToThreadIO()
            .subscribeWith(object :
                DisposableSingleObserver<TopBookBean<ListTopBookBean<TopicBean>>>() {

                override fun onSuccess(t: TopBookBean<ListTopBookBean<TopicBean>>) {
                    mRcd.remove(this)
                    mLivedata.postValue(listUseCase.stretch(t.data?.getList()))

                }

                override fun onError(e: Throwable) {
                    mRcd.remove(this)
                }
            }).add(mRcd)
    }

    override fun onCleared() {
        super.onCleared()
        mRcd.dispose()
    }
}

