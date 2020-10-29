package com.yu.zz.up

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.common.base.BaseViewHolder

class GradationAdapter : RecyclerView.Adapter<GradationViewHolder>() {
    private val mList: MutableList<GradationInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradationViewHolder {
        return GradationViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GradationViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }
}

class GradationViewHolder private constructor(view: View) : BaseViewHolder<GradationInfo>(view) {
    private val mTv: TextView = itemView as TextView

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))

    override fun bind(bean: GradationInfo, position: Int) {
        super.bind(bean, position)
        if (bean.isList()) {
            mTv.text = bean.getList().listName
        }
        if (bean.isGroup()) {
            mTv.text = bean.getGroup().groupName
        }
    }
}