package com.yu.zz.composite

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.createViewModelActivity
import com.yu.zz.common.arrange.createViewModel as creteVM

open class CompositeFragment : Fragment() {
    protected open fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return creteVM(clazz)
    }

    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return creteVM()
    }

    protected open fun <T : ViewModel> createViewModelByActivity(clazz: Class<T>): T {
        return createViewModelActivity(requireActivity(), clazz)
    }
}