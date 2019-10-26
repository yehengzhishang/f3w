package com.yu.zz

import android.app.Application
import com.yu.zz.debug.DebugManager
import com.yu.zz.fwww.R

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DebugManager.INSTANCE.init(this)
        DebugManager.INSTANCE.themeId = R.style.AppTheme_NoActionBar
    }
}