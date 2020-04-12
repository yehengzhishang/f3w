package com.yu.zz.composite

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.common.base.UI
import com.yu.zz.common.base.createViewModelActivity

abstract class CompositeActivity : BaseActivity(), UI {
    override val mUiContext: Context
        get() = this
    override val mLifecycleOwner: LifecycleOwner
        get() = this

    protected fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createViewModelActivity(this, clazz)
    }
}