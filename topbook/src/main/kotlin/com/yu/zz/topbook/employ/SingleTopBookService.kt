package com.yu.zz.topbook.employ

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SingleTopBookService {
    @GET(TOPBOOK_URL_PATH_PREFIX)
    fun getArticleWithCategoryId(@Path(TOPBOOK_PATH_CATEGORY_ID) categoryId: String, @Query("start") start: String, @Query("limit") limit: String): Single<ArticleResponseTopBookBean>

    @GET(TOPBOOK_URL_PAGE)
    fun getListCategory(@Query("start") start: String, @Query("limit") limit: String): Single<CategoryResponseTopBookBean>
}