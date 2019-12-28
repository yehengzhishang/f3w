package com.yu.zz.common.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun getFactoryDefaultCall(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()
fun getFactoryDefaultConverter(): Converter.Factory = GsonConverterFactory.create()

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
    open fun getClientBuilder(): OkHttpClient.Builder = ClientFactory().getClientBuilder(getInterceptors())
    open fun getLogger(): Interceptor? = LoggerInterceptor().logger
    open fun getInterceptors(): List<Interceptor> {
        val interceptors = mutableListOf<Interceptor>()
        val logger = getLogger()
        logger?.apply {
            if (isDebug) {
                interceptors.add(this)
            }
        }
        return interceptors
    }
}

class LoggerInterceptor {
    val logger: Interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BASIC
    }
}

class ClientFactory {
    fun getClientBuilder(interceptors: List<Interceptor>): OkHttpClient.Builder = OkHttpClient.Builder()
            .apply {
                for (i in interceptors) {
                    this.addNetworkInterceptor(i)
                }
            }
}