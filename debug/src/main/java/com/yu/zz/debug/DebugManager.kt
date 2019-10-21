package com.yu.zz.debug

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import androidx.annotation.StyleRes
import kotlin.math.sqrt

class DebugManager private constructor() {
    @StyleRes
    var themeId: Int = R.style.Theme_AppCompat

    companion object {
        val INSTANCE: DebugManager = DebugManager()
    }
}

/**
 * 摇一摇帮助类
 */
private class ShakeHelper(private val mShakeCallBack: ShakeCallBack) : SensorEventListener2 {
    //速度阀值
    private val mSpeed = 7000
    //时间间隔
    private val mInterval = 50
    //上一次摇晃的时间
    private var mLastTime: Long = 0
    //上一次的x、y、z坐标
    private var mLastX: Float = 0.toFloat()
    private var mLastY: Float = 0.toFloat()
    private var mLastZ: Float = 0.toFloat()
    override fun onFlushCompleted(sensor: Sensor) {
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - mLastTime < mInterval) {
            return
        }
        //将NowTime赋给LastTime
        mLastTime = nowTime
        //获取x,y,z
        val nowX = event.values[0]
        val nowY = event.values[1]
        val nowZ = event.values[2]
        //计算x,y,z变化量
        val deltaX = nowX - mLastX
        val deltaY = nowY - mLastY
        val deltaZ = nowZ - mLastZ
        //赋值
        mLastX = nowX
        mLastY = nowY
        mLastZ = nowZ
        //计算
        val nowSpeed = sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / mInterval * 10000
        //判断
        if (nowSpeed >= mSpeed) {
            // 开启debug界面
            mShakeCallBack.shake()
        }
    }
}

/**
 * [ShakeHelper] 摇一摇事件回调
 * [ShakeHelper.onSensorChanged] [ShakeHelper.mShakeCallBack]
 */
interface ShakeCallBack {
    fun shake()
}