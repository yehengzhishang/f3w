package com.yu.zz.tb

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer as OB
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.yu.zz.tb.deep.TopBookApi
import com.yu.zz.tb.deep.TopBookBean
import com.yu.zz.tb.technique.DataBean
import com.yu.zz.tb.technique.TechniqueService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainTopBookActivity : AppCompatActivity() {
    private val mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getDataTechnique().observe(this, OB {
            if (it == null) {
                return@OB
            }
            val content = GsonBuilder().setPrettyPrinting().create().toJson(it)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<TopBookBean<DataBean>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookBean<DataBean>) {
                        mDataTechnique.value = t
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}