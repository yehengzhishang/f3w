package com.yu.zz.common.base

import android.content.Context
import androidx.lifecycle.LifecycleOwner

interface UIProvider {
    fun uiContext(): Context
    fun uiLifecycle(): LifecycleOwner
}

interface UI : UIProvider {
    val mUiContext: Context
    val mLifecycleOwner: LifecycleOwner
    override fun uiContext(): Context = mUiContext
    override fun uiLifecycle(): LifecycleOwner = mLifecycleOwner
}

class UiWrapper(override val mUiContext: Context, override val mLifecycleOwner: LifecycleOwner) : UI