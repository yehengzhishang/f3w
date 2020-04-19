package com.yu.zz.topbook.employ

import com.yu.zz.common.getAppConfig
import com.yu.zz.common.net.*

class TopBookApi private constructor(private val fly: FlyNet) : ServiceFactory by fly {
    companion object {
        val INSTANCE = TopBookApi(FlyNet(FlyNetConfig(baseUrl = TOPBOOK_URL_BASE, callFactory = getFactoryDefaultCall(), converterFactory = getFactoryDefaultConverter(), isDebug = getAppConfig().isDebug)))
    }
}