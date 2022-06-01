package com.yu.zz

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.fwww.R
import com.yu.zz.topbook.FoundationTopBookActivity

class SplashActivity : BaseActivity() {
    private val anim: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            startActivity(Intent(this@SplashActivity, FoundationTopBookActivity::class.java))
            finish()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

    }

    override fun layoutId(): Int {
        return R.layout.activity_splash;
    }

    override fun createSecondUi() {

    }

    override fun createThirdData() {
        findViewById<LottieAnimationView>(R.id.lav).addAnimatorListener(anim)
    }
}