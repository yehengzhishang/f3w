package com.yu.zz.timer

import com.yu.zz.common.arrange.getScreenWidth
import com.yu.zz.common.base.BaseActivity
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import kotlinx.android.synthetic.main.activity_timer.*
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class TimerActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_timer

    override fun ui() {
        val lp = fl.layoutParams
        lp.height = getScreenWidth()
        fl.setOnClickListener {

        }
    }

    override fun data() {
    }

    private fun timeInterval(info: TimeInfo) {
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe(object : FlowableSubscriber<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription) {
                    }

                    override fun onNext(t: Long?) {
                    }

                    override fun onError(t: Throwable?) {
                    }
                })
    }
}