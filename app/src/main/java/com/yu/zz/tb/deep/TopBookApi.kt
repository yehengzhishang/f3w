package com.yu.zz.tb.deep

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TopBookApi private constructor() {
    private val clint = OkHttpClient.Builder()
            .build()
    val retrofit = Retrofit.Builder()
            .client(clint)
            .baseUrl(TOPBOOK_URL_BASE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        val INSTANCE = TopBookApi()
    }
}