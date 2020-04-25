package com.yu.zz.bypass.app

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log

@SuppressLint("StaticFieldLeak")
val PROVIDER_ACTIVITY: ActivityProvider = ActivityProvider()
private const val TAG: String = "ActivityProvider"

class ActivityProvider {
    private var mCurrentActivity: Activity? = null

    fun set(target: Activity) {
        this.mCurrentActivity = target
    }

    fun remove(target: Activity) {
        if (mCurrentActivity == target) {
            this.mCurrentActivity = null
        }
    }

    fun getActivityNull(): Activity? {
        if (mCurrentActivity == null) {
            Log.d(TAG, "current activity is null")
        }
        return mCurrentActivity
    }

    fun getActivity(): Activity {
        return getActivityNull() ?: throw RuntimeException("current activity is null")
    }
}