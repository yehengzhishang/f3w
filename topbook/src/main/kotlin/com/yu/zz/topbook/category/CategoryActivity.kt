package com.yu.zz.topbook.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yu.zz.topbook.R
import com.yu.zz.topbook.deep.CategoryTopBookBean

const val KEY_ID_CATEGORY = KEY_CATEGORY_ID

class CategoryActivity : AppCompatActivity() {
    private val bean: CategoryTopBookBean by lazy {
        intent.getSerializableExtra(KEY_ID_CATEGORY)!! as CategoryTopBookBean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topbook_activity_category)
        title = bean.name
        supportFragmentManager.beginTransaction().apply {
            val fragment = CategorySingleFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_ID, bean.categoryId!!.toString())
                }
            }
            add(R.id.fl, fragment)
            commitNow()
        }
    }
}