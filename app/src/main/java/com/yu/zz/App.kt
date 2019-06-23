package com.yu.zz

import android.app.Application
import android.content.Context
import com.yu.zz.debug.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DebugManager.init(BuildConfig.DEBUG, getDebugConfig(), this@App)
    }

    private fun getDebugConfig(): DebugConfig = DebugConfig().apply {
        this.userData = AppUserDebug()
        this.contextCallBack = object : ContextCallBack {
            override fun getTopContext(): Context = this@App
        }
    }
}

private class AppUserDebug : AbsUserData() {
    override fun userId(): String = "123123123132"

    override fun name() = "大呆呆夜"

    override fun vipState() = "年度超级会员"
}

