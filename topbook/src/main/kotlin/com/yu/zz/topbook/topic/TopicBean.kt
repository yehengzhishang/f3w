package com.yu.zz.topbook.topic

import com.google.gson.annotations.SerializedName

data class TopicBean(
        @SerializedName("topicId")
        var topicId: Int? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("abstract")
        var abstract: String? = null,
        @SerializedName("createTime")
        var createTime: String? = null,
        @SerializedName("updateTime")
        var updateTime: String? = null,
        @SerializedName("userId")
        var userId: String? = null,
        @SerializedName("nickname")
        var nickname: String? = null,
        @SerializedName("avatarUrl")
        var avatarUrl: String? = null
)
