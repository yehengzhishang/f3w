package com.yu.zz.biz

import android.app.Activity
import com.yu.zz.common.base.ActivityCallback
import com.yu.zz.common.base.ActivityLifecycleCallback
import com.yu.zz.common.base.AppContext

class BizLifecycle(mCallback: ActivityCallback) : ActivityLifecycleCallback(mCallback) {
    constructor() : this(BizCallback())
}

private class BizCallback : ActivityCallback {
    override fun add(activity: Activity) {
        AppContext.INSTANCE.addActivity(activity)
    }

    override fun remove(activity: Activity) {
        AppContext.INSTANCE.remove(activity)
    }
}