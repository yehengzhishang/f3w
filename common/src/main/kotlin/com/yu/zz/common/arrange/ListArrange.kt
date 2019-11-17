package com.yu.zz.common.arrange

import android.os.Build

fun <T> MutableList<T>.ifRemove(filter: (T) -> Boolean): Boolean {
    if (Build.VERSION.SDK_INT >= 24) {
        return removeIf { filter.invoke(it) }
    }
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}

class ListFilter<T> {
    val filterNull: (T?) -> Boolean = {
        it == null
    }

    fun and(filter: (T) -> Boolean): (T) -> Boolean = {
        this@ListFilter.filterNull.invoke(it) && filter.invoke(it)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> MutableList<T?>.ifRemoveAndReturn(filter: (T?) -> Boolean): MutableList<T> {
    ifRemove { filter.invoke(it) }
    return this as MutableList<T>
}