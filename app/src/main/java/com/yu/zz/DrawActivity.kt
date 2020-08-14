package com.yu.zz

import com.yu.zz.composite.CompositeActivity
import com.yu.zz.fwww.R

/**
 * 过度绘制测试界面
 */
class DrawActivity : CompositeActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_draw
    }

    override fun createSecondUi() {
    }

    override fun createThirdData() {
    }

}