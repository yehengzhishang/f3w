package com.yu.zz.topbook.employ

import com.yu.zz.composite.CompositeActivity

abstract class TopBookActivity : CompositeActivity() {
    protected inline fun <reified T> createService(): T {
        return TopBookApi.INSTANCE.createService(T::class.java)
    }

    protected fun <T> createService(clazz: Class<T>): T {
        return TopBookApi.INSTANCE.createService(clazz)
    }
}