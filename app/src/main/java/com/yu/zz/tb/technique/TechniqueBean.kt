package com.yu.zz.tb.technique

import com.google.gson.annotations.SerializedName

class TechniqueBean {
    @SerializedName("articleId")
    var articleId: Int? = null
    @SerializedName("categoryId")
    var categoryId: Int? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("cover")
    var cover: String? = null
    @SerializedName("createTime")
    var createTime: String? = null
    @SerializedName("like")
    var like: Boolean? = null
    @SerializedName("likeTotal")
    var likeTotal: Int? = null
}

class DataBean {
    @SerializedName("start")
    var start: Int? = null
    @SerializedName("limit")
    var limit: Int? = null
    @SerializedName("total")
    var total: Int? = null
    @SerializedName("items")
    var items: MutableList<TechniqueBean>? = null
}