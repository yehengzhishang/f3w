package com.yu.zz.bypass

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

fun <T : ViewModel> createViewModelActivity(activity: ComponentActivity, clazz: Class<T>): T {
    return createViewModel(activity.viewModelStore, activity.defaultViewModelProviderFactory, clazz)
}

inline fun <reified T : ViewModel> createViewModelActivity(activity: ComponentActivity): T {
    return createViewModelActivity(activity, T::class.java)
}

fun <T : ViewModel> createViewModelFragment(fragment: Fragment, clazz: Class<T>): T {
    return createViewModel(fragment.viewModelStore, fragment.defaultViewModelProviderFactory, clazz)
}

inline fun <reified T : ViewModel> createViewModelFragment(fragment: Fragment): T {
    return createViewModelFragment(fragment, T::class.java)
}

fun <T : ViewModel> createViewModel(store: ViewModelStore, factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProvider(store, factory)[clazz]
}

inline fun <reified T : ViewModel> createViewModel(store: ViewModelStore, factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(store, factory)[T::class.java]
}