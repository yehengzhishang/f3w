package com.yu.zz.composite

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, change: ((T) -> Unit)) {
    observe(owner, OnceLiveDataObserver<T>(this, change))
}

class OnceLiveDataObserver<Info>(private val mLiveData: LiveData<Info>, private val mChange: ((Info) -> Unit)) : Observer<Info> {
    override fun onChanged(info: Info) {
        mChange(info)
        mLiveData.removeObserver(this)
    }
}