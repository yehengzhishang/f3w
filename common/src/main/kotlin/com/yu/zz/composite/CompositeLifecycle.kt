package com.yu.zz.composite

import android.app.Activity
import com.yu.zz.bypass.app.ActivityCallback
import com.yu.zz.bypass.app.ActivityLifecycleCallback
import com.yu.zz.bypass.app.PROVIDER_ACTIVITY

class CompositeLifecycle(mCallback: ActivityCallback) : ActivityLifecycleCallback(mCallback) {
    constructor() : this(CompositeCallback())
}

private class CompositeCallback : ActivityCallback {
    override fun add(activity: Activity) {
        PROVIDER_ACTIVITY.set(activity)
    }

    override fun remove(activity: Activity) {
        PROVIDER_ACTIVITY.remove(activity)
    }
}