package com.yu.zz.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        init()
        ui()
        data()
    }

    @LayoutRes
    abstract fun layoutId(): Int

    open fun init() {

    }

    abstract fun ui()

    abstract fun data()
}