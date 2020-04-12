package com.yu.zz.topbook.employ

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.yu.zz.common.base.createViewModelFragment

open class TopBookFragment : Fragment() {
    protected fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return createViewModelFragment(this, clazz)
    }
}