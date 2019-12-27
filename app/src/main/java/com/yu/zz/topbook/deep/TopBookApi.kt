package com.yu.zz.topbook.deep

import com.yu.zz.common.getAppConfig
import com.yu.zz.common.net.FlyNet
import com.yu.zz.common.net.FlyNetConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TopBookApi private constructor() {
    private val fly: FlyNet = FlyNet(FlyNetConfig(baseUrl = TOPBOOK_URL_BASE, callFactory = RxJava2CallAdapterFactory.create(), converterFactory = GsonConverterFactory.create(), isDebug = getAppConfig().isDebug))
    val retrofit: Retrofit = fly.retrofit

    companion object {
        val INSTANCE = TopBookApi()
    }
}