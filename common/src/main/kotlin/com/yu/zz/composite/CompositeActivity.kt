package com.yu.zz.composite

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.yu.zz.common.base.BaseActivity
import com.yu.zz.common.base.UI

abstract class CompositeActivity() : BaseActivity(), UI {
    override val mUiContext: Context
        get() = this
    override val mLifecycleOwner: LifecycleOwner
        get() = this
}