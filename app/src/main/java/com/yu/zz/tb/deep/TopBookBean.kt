package com.yu.zz.tb.deep

import com.google.gson.annotations.SerializedName

class TopBookBean<T> {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("data")
    var data: T? = null

    fun isSuccess(): Boolean = success ?: false
}