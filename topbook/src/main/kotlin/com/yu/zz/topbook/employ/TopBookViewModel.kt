package com.yu.zz.topbook.employ

import android.app.Application
import com.yu.zz.composite.CompositeViewModel

open class TopBookViewModel(app: Application) : CompositeViewModel(app) {
    override fun <T> createService(clazz: Class<T>): T {
        return TopBookApi.INSTANCE.createService(clazz)
    }
}