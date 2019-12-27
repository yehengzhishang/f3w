package com.yu.zz.common

private var isInit = false
private var IS_DEBUG = false
private var IS_LOG = false

fun config(isDebug: Boolean, isLog: Boolean = IS_LOG) {
    if (isInit) {
        throw RuntimeException("config is init")
    }
    isInit = true
    IS_DEBUG = isDebug
    IS_LOG = isLog
}

data class AppConfig(val isDebug: Boolean, val isLog: Boolean)

fun getAppConfig(): AppConfig = AppConfig(isDebug = IS_DEBUG, isLog = IS_LOG)