package com.yu.zz.composite

import com.yu.zz.bypass.DisposableChange
import com.yu.zz.bypass.MaybeObserverWrapper
import com.yu.zz.bypass.RxObserverWrapper
import com.yu.zz.bypass.SingleObserverWrapper


private val compositeError: ((Throwable) -> Unit) = { e ->
    e.printStackTrace()
}

fun getErrorEmpty(): (DisposableChange, Throwable) -> Unit {
    return { ob, e ->
        compositeError(e)
        ob.disposeSafe()
    }
}

fun <T> getDefaultNext(next: ((T) -> Unit)): ((DisposableChange, T) -> Unit) {
    return { _, bean ->
        next(bean)
    }
}

fun <T> getDefaultComplete(complete: () -> Unit): (RxObserverWrapper<T>) -> Unit {
    return { ob ->
        complete.invoke()
        ob.disposeSafe()
    }
}

fun getEmptyComplete(): (DisposableChange) -> Unit {
    return { ob ->
        ob.disposeSafe()
    }
}

fun <T> getRxObserver(next: ((T) -> Unit),
                      error: (RxObserverWrapper<T>, Throwable) -> Unit = getErrorEmpty(),
                      complete: (RxObserverWrapper<T>) -> Unit = getEmptyComplete()
): RxObserverWrapper<T> {
    return RxObserverWrapper<T>().apply {
        flowNext(next = getDefaultNext(next))
        flowError(error = error)
        flowComplete(complete = complete)
    }
}

fun <T> getObserverSingle(success: (T) -> Unit,
                          error: (SingleObserverWrapper<T>, Throwable) -> Unit = getErrorEmpty()
): SingleObserverWrapper<T> {
    return SingleObserverWrapper(
            success = getDefaultNext(success),
            error = error
    )
}

fun <T> getObserverMaybe(success: ((T) -> Unit),
                         error: (MaybeObserverWrapper<T>, Throwable) -> Unit = getErrorEmpty(),
                         complete: (MaybeObserverWrapper<T>) -> Unit = getEmptyComplete()): MaybeObserverWrapper<T> {
    return MaybeObserverWrapper(
            success = getDefaultNext(success),
            complete = complete,
            error = error
    )
}
