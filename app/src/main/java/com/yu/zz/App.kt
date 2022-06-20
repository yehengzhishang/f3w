package com.yu.zz

import android.app.Application
import android.util.Log
import com.yu.zz.bypass.app.config
import com.yu.zz.composite.compositeInit
import com.yu.zz.debug.DebugManager
import com.yu.zz.fwww.BuildConfig
import com.yu.zz.fwww.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("rain", "app onCreate" + android.os.Process.myPid())
        // app config
        val isDebug = BuildConfig.DEBUG
        config(isDebug, isDebug)
        // 模块内容初始化
        compositeInit(this)
        DebugManager.INSTANCE.init(this)
        DebugManager.INSTANCE.themeId = R.style.AppTheme_NoActionBar
    }
}
