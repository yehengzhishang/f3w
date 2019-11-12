package com.yu.zz.tb

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.yu.zz.fwww.R
import com.yu.zz.tb.arrange.goToThreadMain
import com.yu.zz.tb.deep.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainTopBookActivity : AppCompatActivity() {
    private val mViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topbook_main)
        mViewModel.getPage()
    }
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    fun getPage(start: Int = 0, limit: Int = 20) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getPageConfig(start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<TopBookPageResponseBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookPageResponseBean) {
                        getItems(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    private fun getItems(bean: TopBookPageResponseBean) {
        if (!bean.isSuccess()) {
            return
        }
        if (bean.data == null) {
            return
        }
        val listBean: TopBookListBean<TopBookPageBean> = bean.data!!
        if (listBean.items == null) {
            return
        }
        val list: MutableList<TopBookPageBean?> = listBean.items!!
        for (itemBean in list) {
            if (itemBean == null) {
                continue
            }
            if (itemBean.categoryId == null) {
                return
            }
            val itemId: String = itemBean.categoryId!!.toString()
            getItem(itemId)
        }
    }

    private fun getItem(itemId: String, start: Int = 0, limit: Int = 8) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getTopBookList(itemId, start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<TopBookResponseBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookResponseBean) {
                        val reuslt = GsonBuilder().setPrettyPrinting().create().toJson(t)
                        Log.e("Rain", reuslt)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("rain", "error")
                    }
                })
    }
}