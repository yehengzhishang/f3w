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

class CompositeObserver<Bean> : BaseObserver<Bean>() {
    private val mList = mutableListOf<Observer<Bean>>()

    override fun onComplete() {
        super.onComplete()
        if (isBreak()) {
            return
        }
        for (ob in mList) {
            ob.onComplete()
        }
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        if (isBreak()) {
            return
        }
        for (ob in mList) {
            ob.onSubscribe(d)
        }
    }

    override fun onNext(bean: Bean) {
        super.onNext(bean)
        if (isBreak()) {
            return
        }
        for (ob in mList) {
            ob.onNext(bean)
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        if (isBreak()) {
            return
        }
        for (ob in mList) {
            ob.onError(e)
        }
    }

    private fun isBreak(): Boolean = mList.size == 0
}