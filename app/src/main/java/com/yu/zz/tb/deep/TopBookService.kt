package com.yu.zz.tb.deep

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopBookService {
    @GET(TOPBOOK_URL_PATH_PREFIX)
    fun getTopBookList(@Path("index") index: String, @Query("start") start: String, @Query("limit") limit: String): Observable<ResponseTopBookBean>

    @GET(TOPBOOK_URL_PAGE)
    fun getPageConfig(@Query("start") start: String, @Query("limit") limit: String): Observable<PageResponseTopBookBean>
}