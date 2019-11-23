package com.yu.zz.common.arrange

import android.content.Context
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.StringRes

private const val MESSAGE_DURATION = Toast.LENGTH_SHORT

fun Context.getScreenHeight(): Int = applicationContext.resources.displayMetrics.heightPixels
fun Context.getScreenWidth(): Int = applicationContext.resources.displayMetrics.widthPixels
fun Context.dp2px(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

fun Context.toast(content: String, duration: Int = MESSAGE_DURATION) {
    Toast.makeText(this, content, duration).show()
}

fun Context.toast(resId: Int, @StringRes duration: Int = MESSAGE_DURATION) {
    Toast.makeText(this, resId, duration).show()
}