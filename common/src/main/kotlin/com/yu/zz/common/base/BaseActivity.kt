package com.yu.zz.common.base

import android.os.Bundle
import android.os.Process.myPid
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("rain", javaClass.name + " onCreate")
        val pid = myPid()
        Log.e("rain", "pid = $pid")

        setContentView(layoutId())
        createFirstInit()
        createSecondUi()
        createThirdData()
    }

    override fun onStart() {
        Log.e("rain", javaClass.name + " onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.e("rain", javaClass.name + " onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.e("rain", javaClass.name + " onPause")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.e("rain", javaClass.name + " onStop")
    }

    override fun onDestroy() {
        Log.e("rain", javaClass.name + " onDestroy")
        super.onDestroy()
    }

    @LayoutRes
    abstract fun layoutId(): Int

    open fun createFirstInit() {

    }

    abstract fun createSecondUi()

    abstract fun createThirdData()
}