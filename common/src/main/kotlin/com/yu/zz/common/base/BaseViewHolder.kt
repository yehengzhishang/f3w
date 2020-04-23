package com.yu.zz.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface HolderBinder<T> {
    fun bindAny(bean: Any, position: Int)
    fun bind(bean: T, position: Int)
}

open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), HolderBinder<T> {
    override fun bindAny(bean: Any, position: Int) {
        @Suppress("UNCHECKED_CAST")
        bind(bean as T, position)
    }

    override fun bind(bean: T, position: Int) {

    }
}