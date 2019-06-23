package com.yu.zz.debug

import android.app.Application
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import androidx.core.app.ActivityCompat
import com.yu.zz.debug.ui.DebugActivity

class DebugManager private constructor() {
    companion object {
        private lateinit var S_APP: Application
        private var S_CONTEXT_CALLBACK: ContextCallBack? = null
        private var S_USER_DATA: UserData? = null
        private var S_USER_UPDATE: UserUpdateListener? = null

        fun init(isDebug: Boolean, config: DebugConfig, app: Application) {
            S_APP = app
            if (!isDebug) {
                return
            }
            S_CONTEXT_CALLBACK = config.contextCallBack
            S_USER_DATA = config.userData
            val sensorManger = app.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            //传感器管理器
            val sensor = sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManger.registerListener(ShakeHelper(ShakeSkip()), sensor, SensorManager.SENSOR_DELAY_GAME)
        }

        fun registerUserUpdateListener(userUpdate: UserUpdateListener) {
            S_USER_UPDATE = userUpdate
            updateUser()
        }

        fun unregisterUserUpdaterListener() {
            S_USER_UPDATE = null
        }

        fun updateUser(userData: UserData?) {
            S_USER_DATA = userData
            updateUser()
        }

        private fun updateUser() {
            S_USER_UPDATE?.update(S_USER_DATA)
        }

        private fun getContext(): Context {
            return S_CONTEXT_CALLBACK?.getTopContext() ?: S_APP
        }
    }


    /**
     * 跳转监听
     */
    class ShakeSkip : ShakeCallBack {
        override fun shake() {
            val context = getContext()
            val intent = Intent(context, DebugActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ActivityCompat.startActivity(context, intent, null)
        }
    }
}


/**
 * 最上层Context获得
 */
interface ContextCallBack {
    fun getTopContext(): Context?
}

/**
 * 获得用户信息
 */
interface UserData {
    fun name(): String
    fun vipState(): String
    fun userId(): String
}

/**
 * 新的用户信息更新
 */
interface UserUpdateListener {
    fun update(userData: UserData?)
}

abstract class AbsUserData : UserData

/**
 * 设置 debug
 */
class DebugConfig {
    var contextCallBack: ContextCallBack? = null
    var userData: UserData? = null
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
        val nowSpeed = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / mInterval * 10000
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

