package com.yu.zz.common.arrange

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): ViewModel {
    return ViewModelProvider(this.viewModelStore, this.defaultViewModelProviderFactory)[T::class.java]
}