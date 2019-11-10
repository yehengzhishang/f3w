package com.yu.zz.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FlyNet private constructor() {
    private val clint = OkHttpClient.Builder()
            .build()
    val retrofit = Retrofit.Builder()
            .client(clint)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        val INSTNCE = FlyNet()
    }
}