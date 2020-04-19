package com.yu.zz.common.arrange

import android.app.Application
import android.widget.Toast

private var app: Application? = null

fun configToast(application: Application) {
    app = application
}

fun showToast(text: CharSequence) {
    if (app == null) {
        throw RuntimeException("app not init")
    }
    Toast.makeText(app, text, Toast.LENGTH_SHORT).show()
}