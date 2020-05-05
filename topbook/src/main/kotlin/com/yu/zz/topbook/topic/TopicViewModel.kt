package com.yu.zz.topbook.topic

import android.app.Application
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.arrange.toast
import com.yu.zz.topbook.employ.ListTopBookBean
import com.yu.zz.topbook.employ.TopBookBean
import com.yu.zz.topbook.employ.TopBookViewModel

class TopicViewModel(app: Application) : TopBookViewModel(app) {
    private val mService: TopicService by lazy {
        createService(TopicService::class.java)
    }
    private val mDataList: MutableLiveData<MutableList<TopicBean>> by lazy {
        MutableLiveData<MutableList<TopicBean>>()
    }
    private val dataList: LiveData<MutableList<TopicBean>> get() = mDataList


    @UiThread
    private fun onNextList(bean: TopBookBean<ListTopBookBean<TopicBean>>) {
        if (!bean.isSuccess()) {
            getApplication<Application>().toast("加载异常")
            return
        }
        val data = bean.data ?: return
        val list = data.getList()
        mDataList.value= list
    }

    fun loadList(start: Int = 0, limit: Int = 10) {
        mService.requestList(start.toString(), limit = limit.toString())
                .goToThreadMain()
                .subscribe(getNext(this::onNextList))
    }

}