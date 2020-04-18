package com.yu.zz.topbook.employ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yu.zz.common.base.BaseViewHolder

abstract class TopBookViewHolder<Bean> constructor(itemView: View) : BaseViewHolder<Bean>(itemView) {
    val mapBridge = HashMap<String, Any>()
    var click: ((Bean, Int) -> Unit)? = null

    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
}
