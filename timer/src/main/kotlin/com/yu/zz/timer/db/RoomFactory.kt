package com.yu.zz.timer.db

import android.content.Context
import androidx.room.Room

fun getDataBaseRecord(context: Context): RecordDataBase =
        Room.databaseBuilder(context, RecordDataBase::class.java, DATA_BASE_NAME)
                .allowMainThreadQueries()
                .build()