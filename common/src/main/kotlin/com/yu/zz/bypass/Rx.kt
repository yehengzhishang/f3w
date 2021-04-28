package com.yu.zz.bypass

import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

fun <T> Observable<T>.goToThreadMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.goToThreadMain(): Flowable<T> {
    return this.compose(MainThreadTransformer())
}

fun <T> Flowable<T>.goToThreadIO(): Flowable<T> {
    return this.compose(IoThreadTransformer<T>())
}

fun <T> Single<T>.goToThreadMain(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.goToThreadIO(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
}

fun <T> Maybe<T>.goToThreadMain(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.goToThreadMain(): Completable {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.goToThreadIO(): Completable {
    return this.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
}

class MainThreadTransformer<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class IoThreadTransformer<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

}

interface DisposableChange : Disposable {
    fun disposeSafe()
}

class RxObserverWrapper<T> : DisposableObserver<T>(), DisposableChange {
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

    override fun disposeSafe() {
        if (!isDisposed) {
            dispose()
        }
    }
}

class SingleObserverWrapper<T> constructor(
        private val success: ((SingleObserverWrapper<T>, T) -> Unit)?,
        private val error: ((SingleObserverWrapper<T>, Throwable) -> Unit)?
) : DisposableSingleObserver<T>(), DisposableChange {

    override fun onSuccess(t: T) {
        success?.invoke(this, t)
    }

    override fun onError(e: Throwable) {
        error?.invoke(this, e)
    }

    override fun disposeSafe() {
        if (!isDisposed) {
            dispose()
        }
    }
}

class MaybeObserverWrapper<T> constructor(
        private val complete: ((MaybeObserverWrapper<T>) -> Unit)? = null,
        private val success: ((MaybeObserverWrapper<T>, T) -> Unit)? = null,
        private val error: ((MaybeObserverWrapper<T>, Throwable) -> Unit)? = null
) : DisposableMaybeObserver<T>(), DisposableChange {
    override fun onSuccess(t: T) {
        success?.invoke(this, t)
    }

    override fun onError(e: Throwable) {
        error?.invoke(this, e)
    }

    override fun onComplete() {
        complete?.invoke(this)
    }

    override fun disposeSafe() {
        if (!isDisposed) {
            dispose()
        }
    }
}

class RxCompositeDisposable(private val mComposite: CompositeDisposable) : Disposable by mComposite {
    constructor() : this(CompositeDisposable())

    val composite: CompositeDisposable
        get() = mComposite

    fun add(dis: Disposable) {
        composite.add(dis)
    }

    fun <T : Disposable> addAndReturn(dis: T): T {
        add(dis)
        return dis
    }

    fun remove(dis: Disposable): Boolean {
        return composite.remove(dis)
    }
}

fun Disposable.addAndReturn(cd: RxCompositeDisposable): Disposable {
    return cd.addAndReturn(this)
}

fun Disposable.add(cd: RxCompositeDisposable) {
    cd.add(this)
}