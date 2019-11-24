package com.yu.zz.common.util

import android.content.Context

var contextCallback: (() -> Context)? = null
fun toastShow(context: Context, content: String) {
}

fun show(content: String) {
    toastShow(contextCallback!!(), content)
}