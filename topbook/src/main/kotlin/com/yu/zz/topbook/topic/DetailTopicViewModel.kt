package com.yu.zz.topbook.topic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.topbook.employ.TopBookViewModel

class DetailTopicViewModel(app: Application) : TopBookViewModel(app) {
    private val mServiceDetail: DetailTopicService by lazy {
        return@lazy createService(DetailTopicService::class.java)
    }
    private val mDataViewPoint: MutableLiveData<ViewpointInfo> by lazy {
        MutableLiveData<ViewpointInfo>()
    }
    val dataViewPoints: LiveData<ViewpointInfo> get() = mDataViewPoint

    private fun nextViewPoints(start: Int, limit: Int, bean: ViewPointTopicBean) {
        mDataViewPoint.value = ViewpointInfo(start, limit, bean.data?.getList() ?: mutableListOf())
    }

    fun loadViewpoints(topicId: String, start: Int = 0, limit: Int = 10) {
        mServiceDetail.requestViewpointsByTopicId(topicId, start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(getNext { bean -> nextViewPoints(start, limit, bean) })
    }
}

data class ViewpointInfo(val start: Int, val limit: Int, val list: List<ViewPointBean>) {
    fun isRefresh(): Boolean {
        return start == 0
    }
}