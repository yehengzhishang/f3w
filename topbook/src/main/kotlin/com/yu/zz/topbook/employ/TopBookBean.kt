package com.yu.zz.topbook.employ

import com.google.gson.annotations.SerializedName
import com.yu.zz.common.arrange.ListFilter
import com.yu.zz.common.arrange.ifRemoveAndReturn
import java.io.Serializable

open class TopBookBean<T> {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("data")
    var data: T? = null

    fun isSuccess(): Boolean = success ?: false
}

class ListTopBookBean<T> {
    @SerializedName("start")
    var start: Int? = null
    @SerializedName("limit")
    var limit: Int? = null
    @SerializedName("total")
    var total: Int? = null
    @SerializedName("items")
    var items: MutableList<T?>? = null

    fun getList(): MutableList<T> {
        val list = items ?: return mutableListOf()
        return list.ifRemoveAndReturn(ListFilter<T>().filterNull)
    }

    fun hasMore(): Boolean {
        val limit = this.limit ?: return false
        val start = this.start ?: return false
        val total = this.total ?: return false
        return start + limit < total
    }

}

class ArticleTopBookBean {
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

class ArticleResponseTopBookBean : TopBookBean<ListTopBookBean<ArticleTopBookBean>>()


class CategoryTopBookBean : Serializable {
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

class CategoryResponseTopBookBean : TopBookBean<ListTopBookBean<CategoryTopBookBean>>()