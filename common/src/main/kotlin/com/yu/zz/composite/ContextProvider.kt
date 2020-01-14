package com.yu.zz.composite

import android.content.Context
import com.yu.zz.common.base.PROVIDER_ACTIVITY
import com.yu.zz.common.base.getProviderApplication

fun getCurrentContext(): Context = PROVIDER_ACTIVITY.getActivityNull() ?: getProviderApplication()