package com.yu.zz.topbook.employ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class TopBookViewHolder<Bean> private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mapBridge = HashMap<String, Any>()
    var click: ((Bean, Int) -> Unit)? = null

    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    @Suppress("UNCHECKED_CAST")
    open fun bindAny(obj: Any, position: Int) {
        bind(obj as Bean, position)
    }

    abstract fun bind(bean: Bean, position: Int)
}
