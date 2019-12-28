package com.yu.zz.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application

class AppContext private constructor() {
    private var currentActivity: Activity? = null
    private var app: Application? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        val INSTANCE = AppContext()
    }

    fun addActivity(context: Activity) {
        this.currentActivity = context
    }

    fun remove(context: Activity) {
        if (this.currentActivity == context) {
            this.currentActivity = null
        }
    }

    fun getActivity(): Activity {
        if (currentActivity == null) {
            throw ActivityNullException("cant find currentActivity")
        }
        return currentActivity!!
    }

    fun getApplication(): Application {
        if (app != null) {
            return app!!
        }
        return getActivity().application
    }
}

class ActivityNullException(content: String) : RuntimeException(content)