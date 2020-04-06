package com.yu.zz.composite

import android.app.Application
import com.yu.zz.common.arrange.RxCompositeDisposable
import com.yu.zz.common.arrange.RxObserverWrapper
import com.yu.zz.common.base.BaseViewModel

open class CompositeViewModel(app: Application) : BaseViewModel(app) {
    protected val mDisposables = RxCompositeDisposable()

    protected fun <T> getNext(next: ((T) -> Unit)): RxObserverWrapper<T> {
        return mDisposables.addAndReturn(getRxObserver(next))
    }
}