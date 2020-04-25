package com.yu.zz.composite

import androidx.lifecycle.ViewModel
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.common.arrange.createViewModel as createVM

abstract class CompositeActivity : BaseActivity() {
    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return createVM()
    }

    protected fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createVM(clazz)
    }
}