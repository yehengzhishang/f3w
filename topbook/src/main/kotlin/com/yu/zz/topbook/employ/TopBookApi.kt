package com.yu.zz.topbook.employ

import com.yu.zz.bypass.*
import com.yu.zz.bypass.app.getAppConfig

class TopBookApi private constructor(private val fly: FlyNet) : ServiceFactory by fly {
    companion object {
        val INSTANCE = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = TopBookApi(FlyNet(FlyNetConfig(baseUrl = TOPBOOK_URL_BASE, callFactory = getFactoryDefaultCall(), converterFactory = getFactoryDefaultConverter(), isDebug = getAppConfig().isDebug)));
    }
}