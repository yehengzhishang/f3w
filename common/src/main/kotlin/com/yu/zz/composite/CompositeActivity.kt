package com.yu.zz.composite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.bypass.createViewModel as createVMByFactory
import com.yu.zz.common.arrange.createViewModel as createVM

abstract class CompositeActivity : BaseActivity() {
    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return createVM()
    }

    protected open fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createVM(clazz)
    }

    protected inline fun <reified T : ViewModel> createViewModel(viewModelStore: ViewModelStore, factory: ViewModelProvider.Factory): T {
        return createVMByFactory(viewModelStore, factory, T::class.java)
    }

    protected open fun <T : ViewModel> createViewModel(viewModelStore: ViewModelStore, factory: ViewModelProvider.Factory, clazz: Class<T>): T {
        return createVMByFactory(viewModelStore, factory, clazz)
    }
}