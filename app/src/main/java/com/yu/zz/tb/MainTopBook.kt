package com.yu.zz.tb

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.yu.zz.fwww.R
import com.yu.zz.tb.arrange.goToThreadMain
import com.yu.zz.tb.deep.TopBookApi
import com.yu.zz.tb.deep.TopBookBean
import com.yu.zz.tb.technique.DataBean
import com.yu.zz.tb.technique.TechniqueService
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_topbook_main.*
import androidx.lifecycle.Observer as OB

class MainTopBookActivity : AppCompatActivity() {
    private val mViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topbook_main)
        mViewModel.getDataTechnique().observe(this, OB {
            if (it == null) {
                return@OB
            }
            val content = GsonBuilder().setPrettyPrinting().create().toJson(it)
            tvContent.text = content
        })
        mViewModel.getTechnique()
    }
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val mDataTechnique: MutableLiveData<TopBookBean<DataBean>> by lazy { MutableLiveData<TopBookBean<DataBean>>() }

    fun getDataTechnique(): LiveData<TopBookBean<DataBean>> = mDataTechnique

    fun getTechnique(start: Int = 0, limit: Int = 8) {
        TopBookApi.INSTANCE.retrofit.create(TechniqueService::class.java)
                .getTechnique(start.toString(), limit.toString())
                .goToThreadMain()
                .subscribe(object : Observer<TopBookBean<DataBean>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookBean<DataBean>) {
                        mDataTechnique.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.e("rain", "error")
                    }
                })
    }
}