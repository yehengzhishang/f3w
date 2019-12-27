package com.yu.zz.common.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class FlyNet constructor(config: FlyNetConfig) {
    val retrofit: Retrofit = Retrofit.Builder()
            .client(config.getClient())
            .baseUrl(config.baseUrl)
            .addCallAdapterFactory(config.callFactory)
            .addConverterFactory(config.converterFactory)
            .build()
}

open class FlyNetConfig constructor(val isDebug: Boolean, val baseUrl: String, val callFactory: CallAdapter.Factory, val converterFactory: Converter.Factory) {
    open fun getClient(): OkHttpClient = getClientBuilder().build()
    open fun getClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
            .apply {
                if (isDebug) {
                    this.addNetworkInterceptor(getLogger())
                }
            }


    open fun getLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BASIC
    }
}