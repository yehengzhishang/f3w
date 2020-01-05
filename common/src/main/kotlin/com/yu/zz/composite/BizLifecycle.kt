package com.yu.zz.composite

import android.app.Activity
import com.yu.zz.common.base.ActivityCallback
import com.yu.zz.common.base.ActivityLifecycleCallback
import com.yu.zz.common.base.PROVIDER_ACTIVITY

class BizLifecycle(mCallback: ActivityCallback) : ActivityLifecycleCallback(mCallback) {
    constructor() : this(BizCallback())
}

private class BizCallback : ActivityCallback {
    override fun add(activity: Activity) {
        PROVIDER_ACTIVITY.set(activity)
    }

    override fun remove(activity: Activity) {
        PROVIDER_ACTIVITY.remove(activity)
    }
}