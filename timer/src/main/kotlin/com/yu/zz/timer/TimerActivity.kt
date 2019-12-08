package com.yu.zz.timer

import com.yu.zz.common.arrange.getScreenWidth
import com.yu.zz.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_timer

    override fun ui() {
        val lp = fl.layoutParams
        lp.height = getScreenWidth()
    }

    override fun data() {
        
    }
}