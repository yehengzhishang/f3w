package com.yu.zz.topbook.category

import android.os.Bundle
import com.yu.zz.topbook.R
import com.yu.zz.topbook.deep.CategoryTopBookBean
import com.yu.zz.topbook.deep.employ.TopBookActivity

const val KEY_ID_CATEGORY = KEY_CATEGORY_ID

class CategoryActivity : TopBookActivity() {
    private val bean: CategoryTopBookBean by lazy {
        intent.getSerializableExtra(KEY_ID_CATEGORY)!! as CategoryTopBookBean
    }

    override fun layoutId(): Int {
        return R.layout.topbook_activity_category
    }

    override fun createSecondUi() {
        title = bean.name
        supportFragmentManager.beginTransaction().apply {
            val fragment = CategorySingleFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_ID, bean.categoryId!!.toString())
                }
            }
            add(R.id.fl, fragment)
            commitAllowingStateLoss()
        }
    }

    override fun createThirdData() {
    }
}