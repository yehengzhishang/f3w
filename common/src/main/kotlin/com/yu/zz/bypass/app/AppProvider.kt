package com.yu.zz.bypass.app

import android.app.Application
import android.util.Log

private var provider: AppProvider? = null

class AppProvider constructor(val app: Application)

fun appInit(app: Application) {
    if (provider != null) {
        Log.e("rain", "app provider is init")
        return
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