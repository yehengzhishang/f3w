package com.yu.zz.common.arrange

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

fun <T> Observable<T>.goToThreadMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.goToThreadMain(): Flowable<T> {
    return this.compose(MainThreadTransformer())
}

class MainThreadTransformer<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class RxObserverWrapper<T> : DisposableObserver<T>() {
    private var mFlowComplete: ((RxObserverWrapper<T>) -> Unit)? = null
    private var mFlowNext: ((RxObserverWrapper<T>, T) -> Unit)? = null
    private var mFlowError: ((RxObserverWrapper<T>, Throwable) -> Unit)? = null
    override fun onComplete() {
        mFlowComplete?.invoke(this)
        Log.e("rain", "complete")
    }

    override fun onNext(t: T) {
        mFlowNext?.invoke(this, t)
        Log.e("rain", "next")
    }

    override fun onError(e: Throwable) {
        mFlowError?.invoke(this, e)
        Log.e("rain", "error")
    }

    internal fun flowNext(next: ((RxObserverWrapper<T>, T) -> Unit)) {
        this.mFlowNext = next
    }

    internal fun flowError(error: ((RxObserverWrapper<T>, Throwable) -> Unit)?) {
        this.mFlowError = error
    }

    internal fun flowComplete(complete: (RxObserverWrapper<T>) -> Unit) {
        this.mFlowComplete = complete
    }

    fun flowDispose() {
        if (!isDisposed) {
            dispose()
        }
    }
}

class RxCompositeDisposable(private val mComposite: CompositeDisposable) : Disposable by mComposite {
    constructor() : this(CompositeDisposable())

    val composite: CompositeDisposable get() = mComposite

    fun add(dis: Disposable) {
        mComposite.add(dis)
    }

    fun <T : Disposable> addAndReturn(dis: T): T {
        add(dis)
        return dis
    }

    fun remove(dis: Disposable): Boolean {
        return mComposite.remove(dis)
    }
}