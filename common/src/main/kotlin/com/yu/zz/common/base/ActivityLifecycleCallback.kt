package com.yu.zz.common.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

open class ActivityLifecycleCallback(private val mCallback: ActivityCallback) : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        mCallback.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mCallback.add(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }
}

interface ActivityCallback {
    fun add(activity: Activity)
    fun remove(activity: Activity)
}