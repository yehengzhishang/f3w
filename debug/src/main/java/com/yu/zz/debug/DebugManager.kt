package com.yu.zz.debug

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import androidx.annotation.StyleRes
import com.yu.zz.debug.ui.DebugActivity
import kotlin.math.sqrt

class DebugManager private constructor() : ShakeCallBack {
    private lateinit var app: Application
    override fun shake() {
        app.startActivity(Intent(app, DebugActivity::class.java)
                .apply { addFlags(FLAG_ACTIVITY_NEW_TASK) })
    }

    @StyleRes
    var themeId: Int = R.style.Theme_AppCompat

    companion object {
        val INSTANCE: DebugManager = DebugManager()
    }

    fun init(app: Application) {
        this.app = app
        val sensorManger = app.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //传感器管理器
        val sensor = sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManger.registerListener(ShakeHelper(this), sensor, SensorManager.SENSOR_DELAY_GAME)
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