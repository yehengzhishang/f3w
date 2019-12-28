package com.yu.zz.debug.arrange

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.TypedValue

fun Context.getAttrColor(resId: Int, defaultValue: Int): Int {
    if (this is Application) {
        Log.e("rain", "Application not suggest,the value will be error ")
    }
    val typeValue = TypedValue()
    this.theme.resolveAttribute(resId, typeValue, true)
    val attr = intArrayOf(resId)
    val typeArray = this.theme.obtainStyledAttributes(typeValue.resourceId, attr)
    val value = typeArray.getColor(0, defaultValue)
    typeArray.recycle()
    return value
}