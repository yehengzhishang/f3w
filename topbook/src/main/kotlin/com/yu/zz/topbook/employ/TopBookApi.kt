package com.yu.zz.topbook.employ

import com.yu.zz.common.getAppConfig
import com.yu.zz.common.net.FlyNet
import com.yu.zz.common.net.FlyNetConfig
import com.yu.zz.common.net.getFactoryDefaultCall
import com.yu.zz.common.net.getFactoryDefaultConverter
import retrofit2.Retrofit

class TopBookApi private constructor() {
    private val fly: FlyNet = FlyNet(FlyNetConfig(baseUrl = TOPBOOK_URL_BASE, callFactory = getFactoryDefaultCall(), converterFactory = getFactoryDefaultConverter(), isDebug = getAppConfig().isDebug))
    val retrofit: Retrofit = fly.retrofit

    companion object {
        val INSTANCE = TopBookApi()
    }
}