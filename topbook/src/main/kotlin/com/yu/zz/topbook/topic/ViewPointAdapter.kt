package com.yu.zz.topbook.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yu.zz.topbook.databinding.TopbookViewpointItemBinding
import com.yu.zz.topbook.employ.TopBookViewHolder

class ViewPointAdapter : RecyclerView.Adapter<ViewPointViewHolder>() {
    private val mListBean = mutableListOf<ViewPointBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPointViewHolder {
        return ViewPointViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mListBean.size
    }

    override fun onBindViewHolder(holder: ViewPointViewHolder, position: Int) {
        holder.bind(mListBean[position], position)
    }

    fun add(list: List<ViewPointBean>) {
        mListBean.addAll(list)
    }

    fun clear() {
        mListBean.clear()
    }
}

class ViewPointViewHolder private constructor(private val mViewBinding: TopbookViewpointItemBinding) : TopBookViewHolder<ViewPointBean>(mViewBinding.root) {
    constructor(parent: ViewGroup) : this(TopbookViewpointItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun bind(bean: ViewPointBean, position: Int) {
        super.bind(bean, position)
        Glide.with(mViewBinding.viewpointIvHead)
                .load(bean.avatarUrl)
                .into(mViewBinding.viewpointIvHead)
        mViewBinding.viewpointTvPickName.text = bean.nickname
        mViewBinding.viewpointTvTime.text = bean.createTime
        mViewBinding.viewpointTvContent.text = bean.content
    }
}