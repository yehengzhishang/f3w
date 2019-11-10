package com.yu.zz.tb

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.http.Query

class MainTopBookActivity : AppCompatActivity() {

}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val mDataTechnique: MutableLiveData<TechniqueBean> by lazy { MutableLiveData<TechniqueBean>() }

    fun getDataTechnique(): LiveData<TechniqueBean> = mDataTechnique

    fun getTechnique(start: Int, limit: Int) {
    }
}

interface TopBookServices {
    fun getTechnique(@Query("start") start: String, @Query("limit") limit: Int)
}

class TechniqueBean