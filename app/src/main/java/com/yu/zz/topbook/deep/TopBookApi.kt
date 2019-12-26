package com.yu.zz.topbook.deep

import com.yu.zz.common.net.FlyNet
import com.yu.zz.common.net.FlyNetConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TopBookApi private constructor() {
    private val fly: FlyNet = FlyNet(FlyNetConfig(TOPBOOK_URL_BASE, RxJava2CallAdapterFactory.create(), GsonConverterFactory.create()))
    val retrofit: Retrofit = fly.retrofit

    companion object {
        val INSTANCE = TopBookApi()
    }
}