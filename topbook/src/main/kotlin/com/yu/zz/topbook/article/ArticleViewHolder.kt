package com.yu.zz.topbook.article

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.yu.zz.topbook.databinding.TopbookItemArticleBinding
import com.yu.zz.topbook.employ.ArticleTopBookBean
import com.yu.zz.topbook.employ.TopBookViewHolder

class ArticleViewHolder private constructor(private val binding: TopbookItemArticleBinding) : TopBookViewHolder<ArticleTopBookBean>(binding.root) {
    constructor(parent: ViewGroup) : this(TopbookItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun bind(bean: ArticleTopBookBean, position: Int) {
        itemView.setOnClickListener {
            click?.invoke(bean, position)
        }
        Glide.with(itemView.context).load(bean.cover).into(binding.ivPic)
        binding.tvTitle.text = bean.title
        binding.tvTime.text = bean.createTime
        binding.tvLike.text = bean.likeTotal?.toString()
    }

}