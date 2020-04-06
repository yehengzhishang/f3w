package com.yu.zz.common.arrange

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.goToThreadMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.goToThreadMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

class RxObserverWrapper<T> : DisposableObserver<T>() {
    private var mFlowComplete: ((RxObserverWrapper<T>) -> Unit)? = null
    private var mFlowNext: ((RxObserverWrapper<T>, T) -> Unit)? = null
    private var mFlowError: ((RxObserverWrapper<T>, Throwable) -> Unit)? = null
    override fun onComplete() {
        mFlowComplete?.invoke(this)
    }

    override fun onNext(t: T) {
        mFlowNext?.invoke(this, t)
    }

    override fun onError(e: Throwable) {
        mFlowError?.invoke(this, e)
    }

    internal fun flowNext(next: ((RxObserverWrapper<T>, T) -> Unit)) {
        this.mFlowNext = next
    }

    internal fun flowError(error: ((RxObserverWrapper<T>, Throwable) -> Unit)?) {
        this.mFlowError = error
    }

    fun flowDispose() {
        if (!isDisposed) {
            dispose()
        }
    }
}

class RxCompositeDisposable : Disposable {
    private val mComposite: CompositeDisposable = CompositeDisposable()
    val composite: CompositeDisposable get() = mComposite
    override fun isDisposed(): Boolean {
        return mComposite.isDisposed
    }

    fun add(dis: Disposable) {
        mComposite.add(dis)
    }

    fun <T : Disposable> addAndReturn(dis: T): T {
        mComposite.add(dis)
        return dis
    }

    fun remove(dis: Disposable): Boolean {
        return mComposite.remove(dis)
    }

    override fun dispose() {
        mComposite.dispose()
    }
}