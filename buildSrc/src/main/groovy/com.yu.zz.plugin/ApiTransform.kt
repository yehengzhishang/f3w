package com.yu.zz.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform

class ApiTransform : Transform() {
    override fun getName(): String {
        return "api_transform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf()
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf()
    }
}