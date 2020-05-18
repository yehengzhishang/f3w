package com.yu.zz.topbook.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yu.zz.topbook.databinding.TopbookTopicItemBinding
import com.yu.zz.topbook.employ.TopBookViewHolder

class TopicViewHolder private constructor(private val mViewBinding: TopbookTopicItemBinding) : TopBookViewHolder<TopicBean>(mViewBinding.root) {
    constructor(parent: ViewGroup) : this(TopbookTopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun bind(bean: TopicBean, position: Int) {
        super.bind(bean, position)
        mViewBinding.topicTvLikeTotal.text = "${bean.viewpointsTotal}参加讨论"
        mViewBinding.topicTvTitle.text = bean.title
        mViewBinding.topicTvFirst.text = bean.abstract
        mViewBinding.topicTvHeadCreate.text = bean.nickname
        val rc: RoundedCorners = RoundedCorners(150)
        Glide.with(mViewBinding.topicIvHeadCreate)
                .load(bean.avatarUrl)
                .apply(RequestOptions.bitmapTransform(rc).override(300, 300))
                .into(mViewBinding.topicIvHeadCreate)
    }
}

class TopicAdapter : RecyclerView.Adapter<TopicViewHolder>() {
    private val mListBean = mutableListOf<TopicBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mListBean.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(mListBean[position], position)
    }

    fun clearList() {
        mListBean.clear()
    }

    fun addList(list: List<TopicBean>) {
        mListBean.addAll(list)
    }
}