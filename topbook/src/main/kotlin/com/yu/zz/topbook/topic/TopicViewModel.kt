package com.yu.zz.topbook.topic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.arrange.toast
import com.yu.zz.topbook.employ.ListTopBookBean
import com.yu.zz.topbook.employ.TopBookBean
import com.yu.zz.topbook.employ.TopBookViewModel

class TopicViewModel(app: Application, private val repository: ITopicRepository) : TopBookViewModel(app) {
    private val mDataList: MutableLiveData<ListInfo> by lazy {
        MutableLiveData<ListInfo>()
    }
    val dataList: LiveData<ListInfo> get() = mDataList

    private val nextList: (Int, Int, TopBookBean<ListTopBookBean<TopicBean>>) -> Unit = next@{ start, limit, bean ->
        if (!bean.isSuccess()) {
            getApplication<Application>().toast("加载异常")
            return@next
        }
        val data = bean.data ?: return@next
        val list = data.getList()
        mDataList.value = ListInfo(start, limit, list)
    }

    fun loadList(start: Int = 0, limit: Int = 10) {
        repository.loadList(start, limit = limit)
                .goToThreadMain()
                .subscribe(getNext { bean -> nextList(start, limit, bean) })
    }
}

data class ListInfo(private val start: Int, private val limit: Int, val list: List<TopicBean>) {
    fun isNeedClear(): Boolean {
        return 0 == start
    }
}
