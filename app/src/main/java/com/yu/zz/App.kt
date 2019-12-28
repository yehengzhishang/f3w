package com.yu.zz

import android.app.Application
import com.yu.zz.common.appInit
import com.yu.zz.common.config
import com.yu.zz.biz.BizLifecycle
import com.yu.zz.debug.DebugManager
import com.yu.zz.fwww.BuildConfig
import com.yu.zz.fwww.R

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val isDebug = BuildConfig.DEBUG
        appInit(this)
        config(isDebug, isDebug)
        this.registerActivityLifecycleCallbacks(BizLifecycle())
        DebugManager.INSTANCE.init(this)
        DebugManager.INSTANCE.themeId = R.style.AppTheme_NoActionBar
    }
}
