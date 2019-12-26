package com.yu.zz.common.net

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class FlyNet constructor(private val config: FlyNetConfig) {
    val retrofit: Retrofit = Retrofit.Builder()
            .client(config.getClient())
            .baseUrl(config.baseUrl)
            .addCallAdapterFactory(config.callFactory)
            .addConverterFactory(config.converterFactory)
            .build()
}

open class FlyNetConfig constructor(val baseUrl: String, val callFactory: CallAdapter.Factory, val converterFactory: Converter.Factory) {
    open fun getClient(): OkHttpClient = OkHttpClient.Builder().build()
}