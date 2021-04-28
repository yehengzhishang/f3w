package com.yu.zz.composite

import android.app.Application
import com.yu.zz.bypass.MaybeObserverWrapper
import com.yu.zz.bypass.RxCompositeDisposable
import com.yu.zz.bypass.RxObserverWrapper
import com.yu.zz.bypass.SingleObserverWrapper
import com.yu.zz.common.base.BaseViewModel

open class CompositeViewModel(app: Application) : BaseViewModel(app) {
    protected val mDisposables = RxCompositeDisposable()

    protected fun <T> getNext(next: ((T) -> Unit)): RxObserverWrapper<T> {
        return mDisposables.addAndReturn(getRxObserver(next))
    }

    protected fun <T> getNextComplete(next: (T) -> Unit, complete: () -> Unit): RxObserverWrapper<T> {
        return mDisposables.addAndReturn(getRxObserver(next = next, complete = getDefaultComplete(complete)))
    }

    protected fun <T> getSuccess(success: ((T) -> Unit)): SingleObserverWrapper<T> {
        return mDisposables.addAndReturn(getObserverSingle(success = success))
    }

    protected fun <T> getMaybe(success: ((T) -> Unit)): MaybeObserverWrapper<T> {
        return mDisposables.addAndReturn(getObserverMaybe(success = success))
    }

    override fun onCleared() {
        super.onCleared()
        mDisposables.dispose()
    }
}