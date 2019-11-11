package com.yu.zz.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FlyNet private constructor() {
    private val clint = OkHttpClient.Builder()
            .build()
    val retrofit = Retrofit.Builder()
            .client(clint)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        val INSTNCE = FlyNet()
    }
}