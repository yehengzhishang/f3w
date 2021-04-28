package com.yu.zz.topbook.topic

import com.yu.zz.topbook.employ.TOPBOOK_URL_TOPIC_LIST
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicService {
    @GET(TOPBOOK_URL_TOPIC_LIST)
    fun requestList(@Query("start") start: String, @Query("limit") limit: String): Single<ResultTopicBean>
}