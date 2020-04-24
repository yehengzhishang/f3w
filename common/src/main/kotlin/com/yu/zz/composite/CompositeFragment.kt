package com.yu.zz.composite

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.createViewModelActivity
import com.yu.zz.bypass.createViewModelFragment

open class CompositeFragment : Fragment() {
    protected open fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createViewModelFragment(this, clazz)
    }

    protected open fun <T : ViewModel> createViewModelByActivity(clazz: Class<T>): T {
        return createViewModelActivity(requireActivity(), clazz)
    }
}