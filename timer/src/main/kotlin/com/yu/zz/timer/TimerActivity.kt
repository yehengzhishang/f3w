package com.yu.zz.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.common.arrange.getScreenWidth
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.timer.db.RecordBean
import com.yu.zz.timer.db.RecordDataBase
import com.yu.zz.timer.db.getDataBaseRecord
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import kotlinx.android.synthetic.main.activity_timer.*
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class TimerActivity : BaseActivity() {
    private val mTimeInfo: TimeInfo by lazy {
        TimerManager().getSettingTime(this)
    }

    private val mDataBaseRecord: RecordDataBase by lazy {
        getDataBaseRecord(this)
    }

    override fun layoutId(): Int {
        return R.layout.activity_timer
    }

    override fun createSecondUi() {
        title = "Just Do It"
        val lp = fl.layoutParams
        lp.height = getScreenWidth()
        fl.setOnClickListener {
            timeInterval(mTimeInfo)
        }
    }

    override fun createThirdData() {
    }

    private fun timeInterval(info: TimeInfo) {
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .goToThreadMain()
                .subscribe(object : FlowableSubscriber<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(s: Subscription) {
                    }

                    override fun onNext(t: Long) {
                        val showTime = info.getTimeStr(t)
                        tvShowTime.text = showTime
                    }

                    override fun onError(t: Throwable?) {
                    }
                })
    }
}

class RecordAdapter : RecyclerView.Adapter<RecordViewHolder>() {
    private val mList = mutableListOf<RecordBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder = RecordViewHolder(parent)

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
    }

}

class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false))
}