package com.yu.zz.topbook.article

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import com.yu.zz.topbook.employ.ArticleTopBookBean

fun articleClick(activity: Activity): (bean: ArticleTopBookBean, position: Int) -> Unit {
    return { _, _ ->
        activity.window.decorView.let { view ->
            Snackbar.make(view, "文章详情页面正在生成中", Snackbar.LENGTH_SHORT).show()
        }
    }
}