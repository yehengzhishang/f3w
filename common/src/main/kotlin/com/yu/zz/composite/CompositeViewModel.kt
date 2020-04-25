package com.yu.zz.composite

import android.app.Application
import com.yu.zz.bypass.RxCompositeDisposable
import com.yu.zz.bypass.RxObserverWrapper
import com.yu.zz.common.base.BaseViewModel

open class CompositeViewModel(app: Application) : BaseViewModel(app) {
    protected val mDisposables = RxCompositeDisposable()

    protected fun <T> getNext(next: ((T) -> Unit)): RxObserverWrapper<T> {
        return mDisposables.addAndReturn(getRxObserver(next))
    }

    protected fun <T> getNextComplete(next: (T) -> Unit, complete: () -> Unit): RxObserverWrapper<T> {
        return mDisposables.addAndReturn(getRxObserver(next = next, complete = getDefaultComplete(complete)))
    }

    open protected fun <T> createService(clazz: Class<T>): T? {
        return null
    }

    override fun onCleared() {
        super.onCleared()
        mDisposables.dispose()
    }
}