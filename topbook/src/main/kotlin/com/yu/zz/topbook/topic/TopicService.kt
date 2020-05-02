package com.yu.zz.topbook.topic

import com.yu.zz.topbook.employ.ListTopBookBean
import com.yu.zz.topbook.employ.TOPBOOK_URL_TOPIC_LIST
import com.yu.zz.topbook.employ.TopBookBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicService {
    @GET(TOPBOOK_URL_TOPIC_LIST)
    fun requestList(@Query("start") start: String, @Query("limit") limit: String): Observable<TopBookBean<ListTopBookBean<TopicBean>>>
}