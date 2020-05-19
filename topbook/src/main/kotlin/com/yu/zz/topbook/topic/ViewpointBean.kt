package com.yu.zz.topbook.topic

import com.google.gson.annotations.SerializedName
import com.yu.zz.topbook.employ.ListTopBookBean
import com.yu.zz.topbook.employ.TopBookBean

data class ViewPointBean(
        @SerializedName("viewpointId")
        var viewpointId: Int? = null,
        @SerializedName("topicId")
        var topicId: Int? = null,
        @SerializedName("content")
        var content: String? = null,
        @SerializedName("totalLike")
        var totalLike: Int? = null,
        @SerializedName("stick")
        var stick: Int? = null,
        @SerializedName("createTime")
        var createTime: String? = null,
        @SerializedName("userId")
        var userId: String? = null,
        @SerializedName("nickname")
        var nickname: String? = null,
        @SerializedName("avatarUrl")
        var avatarUrl: String? = null
)

class ViewPointTopicBean : TopBookBean<ListTopBookBean<ViewPointBean>>()