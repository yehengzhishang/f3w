package com.yu.zz.topbook.topic

import com.yu.zz.topbook.employ.TOPBOOK_URL_TOPIC_VIEWPOINTS
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailTopicService {
    @GET(TOPBOOK_URL_TOPIC_VIEWPOINTS)
    fun requestViewpointsByTopicId(@Query("topic_id") topicId: String, @Query("start") start: String, @Query("limit") limit: String): Observable<ViewPointTopicBean>
}