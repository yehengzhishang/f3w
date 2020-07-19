package com.yu.zz.topbook.employ

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadScroller constructor(private val loadMore: () -> Unit, private val threshold: Int = 5) : RecyclerView.OnScrollListener() {
    private var mIsLoad = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (mIsLoad) {
            return
        }
        Log.e("rain", "dy = $dy")
        if (dy <= 0) {
            return
        }
        val manager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val adapter = recyclerView.adapter ?: return
        val firstPosition = manager.findLastVisibleItemPosition()
        if (firstPosition > adapter.itemCount - threshold) {
            mIsLoad = true
            loadMore()
        }
    }

    fun finishLoad() {
        mIsLoad = false
    }
}