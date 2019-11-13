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
import com.yu.zz.tb.deep.*
import io.reactivex.Observable
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
    private val mMapCategory = HashMap<String, TopBookPageBean>()
    private val mListCategory = mutableListOf<TopBookPageBean>()
    private val mDataCategory by lazy { MutableLiveData<List<TopBookPageBean>>() }

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

    fun getDataCategory(): LiveData<List<TopBookPageBean>> {
        return mDataCategory
    }

    private fun getItems(bean: TopBookPageResponseBean) {
        Observable.just(bean)
                .filter { it.isSuccess() && it.data != null }
                .map { it.data!! }
                .filter { it.items != null }
                .map { it.items!! }
                .flatMap { Observable.fromIterable(it) }
                .filter { it.categoryId != null }
                .doOnNext { addCategory(it!!) }
                .map { it.categoryId!! }
                .flatMap { getObsItem(it.toString()) }
                .goToThreadMain()
                .subscribe(object : Observer<TopBookResponseBean> {
                    override fun onComplete() {
                        refreshData()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: TopBookResponseBean) {
                        val itemId = itemId(t) ?: return
                        if (!mMapCategory.keys.contains(itemId)) {
                            return
                        }
                        Log.e("rain", GsonBuilder().setPrettyPrinting().create().toJson(t))
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    private fun refreshData() {
        mListCategory.sortBy { it.categoryId }
        mDataCategory.postValue(mListCategory)
    }

    private fun addCategory(bean: TopBookPageBean) {
        mMapCategory[bean.categoryId!!.toString()] = bean
        mListCategory.add(bean)
    }


    private fun itemId(t: TopBookResponseBean): String? {
        if (!t.isSuccess()) return null
        return t.data?.items?.getOrNull(0)?.categoryId?.toString()
    }

    private fun getObsItem(itemId: String, start: Int = 0, limit: Int = 8): Observable<TopBookResponseBean> {
        return TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getTopBookList(itemId, start.toString(), limit.toString())
    }

    private fun addItem(itemId: String, bean: TopBookResponseBean) {

    }
}
