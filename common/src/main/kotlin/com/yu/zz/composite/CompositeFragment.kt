package com.yu.zz.composite

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.yu.zz.bypass.createViewModelActivity
import com.yu.zz.bypass.createViewModel as createVMByFactory
import com.yu.zz.common.arrange.createViewModel as creteVM

open class CompositeFragment : Fragment() {
    protected open fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return creteVM(clazz)
    }

    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return creteVM()
    }

    protected open fun <T : ViewModel> createViewModel(viewModelStore: ViewModelStore, factory: ViewModelProvider.Factory, clazz: Class<T>): T {
        return createVMByFactory(viewModelStore, factory, clazz)
    }

    protected open fun <T : ViewModel> createViewModelByActivity(clazz: Class<T>): T {
        return createViewModelActivity(requireActivity(), clazz)
    }
}