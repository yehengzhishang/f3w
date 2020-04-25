package com.yu.zz.composite

import com.yu.zz.bypass.RxObserverWrapper


private var compositeError: ((Throwable) -> Unit) = { e ->
    e.printStackTrace()
}

fun <T> getErrorEmpty(): (RxObserverWrapper<T>, Throwable) -> Unit {
    return { ob, e ->
        compositeError(e)
        ob.flowDispose()
    }
}

fun <T> getDefaultNext(next: ((T) -> Unit)): ((RxObserverWrapper<T>, T) -> Unit) {
    return { _, bean ->
        next(bean)
    }
}

fun <T> getDefaultComplete(complete: () -> Unit): (RxObserverWrapper<T>) -> Unit {
    return { ob ->
        complete.invoke()
        ob.flowDispose()
    }
}

fun <T> getEmptyComplete(): (RxObserverWrapper<T>) -> Unit {
    return { ob ->
        ob.flowDispose()
    }
}

fun <T> getRxObserver(next: ((T) -> Unit),
                      error: (RxObserverWrapper<T>, Throwable) -> Unit = getErrorEmpty(),
                      complete: (RxObserverWrapper<T>) -> Unit = getEmptyComplete<T>()
): RxObserverWrapper<T> {
    return RxObserverWrapper<T>().apply {
        flowNext(next = getDefaultNext(next))
        flowError(error = error)
        flowComplete(complete = complete)
    }
}

