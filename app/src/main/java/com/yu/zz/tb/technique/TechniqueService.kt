package com.yu.zz.tb.technique

import com.yu.zz.tb.deep.TOPBOOK_URL_TECHNIQUE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TopBookService {
    @GET(TOPBOOK_URL_TECHNIQUE)
    fun getTechnique(@Query("start") start: String, @Query("limit") limit: Int): Observable<TechniqueBean>
}