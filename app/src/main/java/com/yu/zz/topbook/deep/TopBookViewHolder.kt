package com.yu.zz.topbook.deep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class TopBookViewHolder<Bean> private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    @Suppress("UNCHECKED_CAST")
    open fun bindAny(obj: Any) {
        bind(obj as Bean)
    }

    abstract fun bind(bean: Bean)
}