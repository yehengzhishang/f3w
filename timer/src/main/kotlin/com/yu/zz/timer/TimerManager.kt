package com.yu.zz.timer

import android.content.Context

class TimerManager {
    fun getSettingTime(context: Context): TimeInfo {
        val sp = context.getSharedPreferences("time", Context.MODE_PRIVATE)
        val time = sp.getLong("default_time", TIME_DEFAULT_EVERY)
        return TimeInfo(time)
    }

}

class TimeInfo constructor(var time: Long) {
    fun getTimeStr(): String {
        val second = time / 1000L
        val min = second / 60L
        val ss = second % 6000L
        return "${min}:${ss}"
    }
}