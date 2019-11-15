package com.yu.zz.topbook.deep

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopBookService {
    @GET(TOPBOOK_URL_PATH_PREFIX)
    fun getArticleWithCategoryId(@Path("categoryId") categoryId: String, @Query("start") start: String, @Query("limit") limit: String): Observable<ArticleResponseTopBookBean>

    @GET(TOPBOOK_URL_PAGE)
    fun getPageConfig(@Query("start") start: String, @Query("limit") limit: String): Observable<CategoryResponseTopBookBean>
}