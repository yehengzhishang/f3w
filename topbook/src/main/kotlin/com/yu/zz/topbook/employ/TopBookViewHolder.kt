package com.yu.zz.topbook.employ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yu.zz.common.base.BaseViewHolder

abstract class TopBookViewHolder<Bean> constructor(itemView: View) : BaseViewHolder<Bean>(itemView) {
    private val bridgeMap = mutableMapOf<String, Any>()
    var click: ((Bean, Int) -> Unit)? = null

    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    @Suppress("UNCHECKED_CAST")
    protected fun <T> bridgeGet(key: String): T {
        return bridgeMap[key] as T
    }

    protected fun bridgeHas(key: String): Boolean {
        return bridgeMap.containsKey(key)
    }

    fun bridgePut(key: String, value: Any) {
        bridgeMap[key] = value
    }
}
