package com.yu.zz.composite

import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.createViewModelActivity
import com.yu.zz.common.base.BaseActivity

abstract class CompositeActivity : BaseActivity() {
    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return createViewModelActivity(this)
    }

    protected fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createViewModelActivity(this, clazz)
    }
}