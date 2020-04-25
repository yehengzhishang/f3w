package com.yu.zz.common.arrange

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.createViewModelFragment

fun <T : ViewModel> Fragment.createViewModel(clazz: Class<T>): T {
    return createViewModelFragment(this, clazz)
}

inline fun <reified T : ViewModel> Fragment.createViewModel(): T {
    return createViewModelFragment(this)
}