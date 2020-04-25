package com.yu.zz.common.arrange

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.yu.zz.bypass.OnceLiveDataObserver

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, change: ((T?) -> Unit)) {
    observe(owner, OnceLiveDataObserver(this, change))
}