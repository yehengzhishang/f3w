package com.yu.zz.composite

import android.content.Context
import com.yu.zz.common.base.PROVIDER_ACTIVITY
import com.yu.zz.common.base.getProviderApplication

fun getContext(): Context {
    val activity = PROVIDER_ACTIVITY.getActivityNull()
    if (activity != null) {
        return activity
    }
    return getProviderApplication()
}