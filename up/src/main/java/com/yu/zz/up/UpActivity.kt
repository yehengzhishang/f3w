package com.yu.zz.up

import android.app.Application
import com.yu.zz.composite.CompositeActivity
import com.yu.zz.composite.CompositeViewModel

abstract class UpActivity : CompositeActivity() {

}

class UpViewModel(app: Application) : CompositeViewModel(app)