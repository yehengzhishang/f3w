package com.yu.zz.topbook.topic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yu.zz.topbook.R
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
        Glide.with(mViewBinding.topicIvHeadCreate)
                .load(bean.avatarUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(150)).override(300, 300))
                .into(mViewBinding.topicIvHeadCreate)
    }
}

class TopicAdapter : RecyclerView.Adapter<TopicViewHolder>() {
    private val keyTag = R.id.topic_tv_first
    private val mListBean = mutableListOf<TopicBean>()
    private val mItemClick: View.OnClickListener = View.OnClickListener { view ->
        val obj = view.getTag(keyTag) ?: return@OnClickListener
        if (obj !is TopicBean) {
            return@OnClickListener
        }
        clickBean?.invoke(obj, mListBean.indexOf(obj))
    }
    var clickBean: ((TopicBean, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mListBean.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val bean = mListBean[position]
        holder.itemView.setTag(keyTag, bean)
        holder.itemView.setOnClickListener(mItemClick)
        holder.bind(bean, position)
    }

    fun clearList() {
        mListBean.clear()
    }

    fun addList(list: List<TopicBean>) {
        mListBean.addAll(list)
    }
}