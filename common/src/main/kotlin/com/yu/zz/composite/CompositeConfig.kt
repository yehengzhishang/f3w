package com.yu.zz.composite

import android.app.Application
import com.yu.zz.bypass.app.appInit

fun compositeInit(app: Application) {
    appInit(app)
    app.registerActivityLifecycleCallbacks(CompositeLifecycle())
}