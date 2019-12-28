package com.yu.zz.common.base

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class BaseObserver<Bean> : Observer<Bean> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(bean: Bean) {
    }

    override fun onError(e: Throwable) {
    }
}