package com.yu.zz.topbook.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yu.zz.topbook.databinding.TopbookTopicItemBinding
import com.yu.zz.topbook.employ.TopBookViewHolder

class TopicViewHolder(parent: ViewGroup) : TopBookViewHolder<TopicBean>(parent) {
    private val mViewBinding: TopbookTopicItemBinding = TopbookTopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(bean: TopicBean, position: Int) {
        super.bind(bean, position)
        mViewBinding.topicTitle.text = bean.title
    }
}