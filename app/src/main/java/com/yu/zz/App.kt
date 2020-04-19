package com.yu.zz

import android.app.Application
import com.yu.zz.common.config
import com.yu.zz.composite.compositeInit
import com.yu.zz.debug.DebugManager
import com.yu.zz.fwww.BuildConfig
import com.yu.zz.fwww.R

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // app config
        val isDebug = BuildConfig.DEBUG
        config(isDebug, isDebug)
        compositeInit(this)
        // 模块内容初始化
        DebugManager.INSTANCE.init(this)
        DebugManager.INSTANCE.themeId = R.style.AppTheme_NoActionBar
    }
}
