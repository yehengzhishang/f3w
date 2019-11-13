package com.yu.zz.common.arrange

import android.content.Context
import android.util.TypedValue

fun Context.getScreenHeight(): Int = applicationContext.resources.displayMetrics.heightPixels
fun Context.getScreenWidth(): Int = applicationContext.resources.displayMetrics.widthPixels
fun Context.dp2px(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()