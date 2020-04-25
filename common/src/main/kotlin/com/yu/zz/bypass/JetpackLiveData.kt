package com.yu.zz.bypass

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class OnceLiveDataObserver<Info>(private val mLiveData: LiveData<Info>, private val mChange: ((Info?) -> Unit)) : Observer<Info> {
    override fun onChanged(info: Info) {
        mChange(info)
        mLiveData.removeObserver(this)
    }
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, change: ((T?) -> Unit)) {
    observe(owner, OnceLiveDataObserver(this, change))
}