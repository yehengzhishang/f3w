package com.yu.zz.composite

import android.app.Application
import com.yu.zz.common.arrange.configToast
import com.yu.zz.common.base.appInit

fun compositeInit(app: Application) {
    configToast(app)
    appInit(app)
    app.registerActivityLifecycleCallbacks(CompositeLifecycle())
}