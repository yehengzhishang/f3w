package com.yu.zz.composite

import android.app.Application
import android.content.Context
import com.yu.zz.bypass.app.PROVIDER_ACTIVITY
import com.yu.zz.bypass.app.getProviderApplication

fun getCurrentContext(): Context = PROVIDER_ACTIVITY.getActivityNull() ?: getProviderApplication()

fun getApp(): Application {
    return getProviderApplication()
}