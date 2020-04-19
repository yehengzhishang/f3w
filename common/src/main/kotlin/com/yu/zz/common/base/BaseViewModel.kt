package com.yu.zz.common.base

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.*

const val DEFAULT_FIRST = 0
const val DEFAULT_MESSAGE = ""

fun <T : ViewModel> createViewModelActivity(activity: ComponentActivity, clazz: Class<T>): T {
    return createViewModel(activity.viewModelStore, activity.defaultViewModelProviderFactory, clazz)
}

fun <T : ViewModel> createViewModelFragment(fragment: Fragment, clazz: Class<T>): T {
    return createViewModel(fragment.viewModelStore, fragment.defaultViewModelProviderFactory, clazz)
}

fun <T : ViewModel> createViewModel(store: ViewModelStore, factory: ViewModelProvider.Factory, clazz: Class<T>): T {
    return ViewModelProvider(store, factory)[clazz]
}

open class BaseViewModel(app: Application) : AndroidViewModel(app) {
    private val mDataToast: ProviderData<String> by lazy {
        MessageProviderData()
    }

    private val mDataMessage: PairProviderData<Int, String> by lazy {
        IntAndMsgProviderData(DEFAULT_FIRST, DEFAULT_MESSAGE)
    }

    fun getDataToast(): LiveData<String> = mDataToast.data
    fun getDataMessage(): LiveData<Pair<Int, String>> = mDataMessage.data
}

interface ProviderData<T> {
    val data: LiveData<T>
    fun set(t: T) {
        val data = data
        if (data !is MutableLiveData) {
            return
        }
        val dataMu: MutableLiveData<T> = data
        dataMu.value = t
    }

    fun post(t: T) {
        val data = data
        if (data !is MutableLiveData) {
            return
        }
        val dataMu: MutableLiveData<T> = data
        dataMu.postValue(t)
    }
}

interface PairProviderData<F, S> : ProviderData<Pair<F, S>> {
    val defaultF: F
    val defaultS: S
    fun defaultFirst(s: S) {
        set(Pair(defaultF, s))
    }

    fun defaultSecond(f: F) {
        set(Pair(f, defaultS))
    }

    fun zipSet(f: F, s: S) {
        set(Pair(f, s))
    }
}

class MessageProviderData(override val data: LiveData<String>) : ProviderData<String> {
    constructor() : this(MutableLiveData<String>())
}

class IntAndMsgProviderData(override val data: LiveData<Pair<Int, String>>, override val defaultF: Int, override val defaultS: String) : PairProviderData<Int, String> {
    constructor(code: Int, message: String) : this(MutableLiveData<Pair<Int, String>>(), code, message)
}