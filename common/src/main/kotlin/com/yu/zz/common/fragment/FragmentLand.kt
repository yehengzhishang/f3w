package com.yu.zz.common.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*

interface FragmentLand {

}

interface OnBackPressedHandle {
    fun isInterceptBack(): Boolean
}


fun fragmentOnBackPressHandle(fragment: Fragment): Boolean {
    return fragment.isAdded && fragment.isVisible && fragment.isResumed
            && fragment is OnBackPressedHandle && fragment.isInterceptBack()
}

fun managerOnBackPressedHandle(manager: FragmentManager): Boolean {
    val fragments = manager.fragments
    for (fragment in fragments) {
        if (managerOnBackPressedHandle(fragment.childFragmentManager)) {
            return true
        }
        if (fragmentOnBackPressHandle(fragment)) {
            return true
        }
    }
    return false
}

class FragmentLandHelper {
    private val mStackFlow = ArrayDeque<Fragment>()
    private val mStackAux = ArrayDeque<Fragment>()

    fun go() {

    }

    fun onBackPressed(manager: FragmentManager): Boolean {
        if (managerOnBackPressedHandle(manager)) {
            return true
        }
        return false
    }
}