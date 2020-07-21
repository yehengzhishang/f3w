package com.yu.zz.topbook.topic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.topbook.employ.TopBookViewModel
import io.reactivex.Observable


class DetailViewModelFactory(private val app: Application, private val repo: DetailRepository) : ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailTopicViewModel::class.java)) {
            return DetailTopicViewModel(app, repo) as T
        }
        return super.create(modelClass)
    }
}

class DetailRepository(private val service: DetailTopicService) {
    fun getViewpointByTopicId(topicId: String, start: String, limit: String): Observable<ViewPointTopicBean> {
        return service.requestViewpointsByTopicId(topicId, start, limit)
    }
}

class DetailTopicViewModel(app: Application, private val repo: DetailRepository) : TopBookViewModel(app) {
    private val mDataViewPoint: MutableLiveData<ViewpointInfo> by lazy {
        MutableLiveData<ViewpointInfo>()
    }
    val dataViewPoints: LiveData<ViewpointInfo> get() = mDataViewPoint

    private fun nextViewPoints(start: Int, limit: Int, bean: ViewPointTopicBean) {
        mDataViewPoint.value = ViewpointInfo(start, limit, bean.data?.getList() ?: mutableListOf())
    }

    fun loadViewpoints(topicId: String, start: Int = 0, limit: Int = 10) {
        repo.getViewpointByTopicId(topicId, start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(getNext { bean -> nextViewPoints(start, limit, bean) })
    }
}

data class ViewpointInfo(val start: Int, val limit: Int, val list: List<ViewPointBean>) {
    fun isRefresh(): Boolean {
        return start == 0
    }
}