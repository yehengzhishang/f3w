package com.yu.zz.composite

import com.yu.zz.common.arrange.RxObserverWrapper

private var compositeError: ((Throwable) -> Unit) = { e ->
    e.printStackTrace()
}

fun <T> getDefaultError(): (RxObserverWrapper<T>, Throwable) -> Unit {
    return { ob, e ->
        compositeError(e)
        ob.flowDispose()
    }
}

fun <T> getDefaultNext(next: ((T) -> Unit)): ((RxObserverWrapper<T>, T) -> Unit) {
    return { ob, bean ->
        next(bean)
        ob.flowDispose()
    }
}

fun <T> getObserver(next: ((T) -> Unit)): RxObserverWrapper<T> {
    return RxObserverWrapper<T>().apply {
        flowNext(next = getDefaultNext(next))
        flowError(getDefaultError())
    }
}