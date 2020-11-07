package com.yu.zz.up

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yu.zz.common.base.BaseViewHolder
import com.yu.zz.composite.CompositeActivity
import com.yu.zz.composite.CompositeViewModel

abstract class UpActivity : CompositeActivity() {

}

open class UpViewModel(app: Application) : CompositeViewModel(app)


abstract class UpViewHolder<T> constructor(view: View) : BaseViewHolder<T>(view) {
    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
}