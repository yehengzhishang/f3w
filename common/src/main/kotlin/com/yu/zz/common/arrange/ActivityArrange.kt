package com.yu.zz.common.arrange

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.createViewModelActivity

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): T {
    return createViewModelActivity(this)
}

fun <T : ViewModel> AppCompatActivity.createViewModel(clazz: Class<T>): T {
    return createViewModelActivity(this, clazz)
}