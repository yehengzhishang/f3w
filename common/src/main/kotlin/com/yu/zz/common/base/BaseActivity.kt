package com.yu.zz.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        createFirstInit()
        createSecondUi()
        createThirdData()
    }

    @LayoutRes
    abstract fun layoutId(): Int

    open fun createFirstInit() {

    }

    abstract fun createSecondUi()

    abstract fun createThirdData()
}