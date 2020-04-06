package com.yu.zz.common.base

import android.app.Application
import android.util.Log

private var provider: AppProvider? = null

fun appInit(app: Application) {
    if (provider != null) {
        Log.e("rain", "app provider is init")
    }
    provider = AppProvider(app)
}

fun getAppProvider(): AppProvider {
    if (provider == null) {
        throw RuntimeException("app not init")
    }
    return provider!!
}

fun getProviderApplication(): Application {
    return getAppProvider().app
}

class AppProvider constructor(val app: Application)