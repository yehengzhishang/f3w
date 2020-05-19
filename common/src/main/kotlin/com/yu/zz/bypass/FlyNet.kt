package com.yu.zz.bypass

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun getFactoryDefaultCall(): CallAdapter.Factory {
    return RxJava2CallAdapterFactory.create()
}

fun getFactoryDefaultConverter(): Converter.Factory {
    return GsonConverterFactory.create()
}

interface ServiceFactory {
    fun <T> createService(clazz: Class<T>): T
}

class FlyNet constructor(config: FlyNetConfig) : ServiceFactory {
    private val mRetrofit: Retrofit = Retrofit.Builder()
            .client(config.getClient())
            .baseUrl(config.baseUrl)
            .addCallAdapterFactory(config.callFactory)
            .addConverterFactory(config.converterFactory)
            .build()

    override fun <T> createService(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }
}

open class FlyNetConfig constructor(val isDebug: Boolean, val baseUrl: String, val callFactory: CallAdapter.Factory, val converterFactory: Converter.Factory) {
    open fun getLogger(): Interceptor? {
        return LoggerInterceptor().logger
    }

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

    open fun getClientBuilder(): OkHttpClient.Builder {
        return ClientFactory().getClientBuilder(getInterceptors())
    }

    open fun getClient(): OkHttpClient {
        return getClientBuilder()
                .callTimeout(1, TimeUnit.SECONDS)
                .build()
    }
}

class LoggerInterceptor {
    val logger: Interceptor = createLogger()

    private fun createLogger(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC): Interceptor {
        return HttpLoggingInterceptor().apply {
            this.level = level
        }
    }
}

class ClientFactory {
    fun getClientBuilder(interceptors: List<Interceptor>): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        for (i in interceptors) {
            builder.addNetworkInterceptor(i)
        }
        return builder
    }
}