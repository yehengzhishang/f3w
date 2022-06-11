package com.yu.zz.bypass.app.date

import java.text.SimpleDateFormat
import java.util.*

const val PATTERN = "yyyy-MM-dd"
val timeZoneDefault: TimeZone = TimeZone.getDefault()

fun dateFormat(date: Date): String {
    return SimpleDateFormat(PATTERN, Locale.getDefault()).format(date);
}

class DateAssist(
    private val pattern: String,
    private val timeZone: TimeZone,
    private val locale: Locale
) {
    private val mFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(pattern, locale).apply {
            timeZone = this@DateAssist.timeZone
        }
    }

    fun dateFormat(date: Date): String {
        return mFormat.format(date)
    }
}

