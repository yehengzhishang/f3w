package com.yu.zz.tb.deep

import com.google.gson.annotations.SerializedName

open class TopBookBean<T> {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("data")
    var data: T? = null

    fun isSuccess(): Boolean = success ?: false
}

class TopBookListBean<T> {
    @SerializedName("start")
    var start: Int? = null
    @SerializedName("limit")
    var limit: Int? = null
    @SerializedName("total")
    var total: Int? = null
    @SerializedName("items")
    var items: MutableList<T?>? = null
}

class TopBookDataBean {
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

class TopBookResponseBean : TopBookBean<TopBookListBean<TopBookDataBean>>()


class TopBookPageBean {
    @SerializedName("categoryId")
    var categoryId: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("priority")
    var priority: Int? = null
    @SerializedName("createTime")
    var createTime: String? = null
    @SerializedName("updateTime")
    var updateTime: String? = null
}

class TopBookPageResponseBean : TopBookBean<TopBookListBean<TopBookPageBean>>()