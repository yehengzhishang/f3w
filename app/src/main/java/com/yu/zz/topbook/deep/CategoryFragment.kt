package com.yu.zz.topbook.deep

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.yu.zz.fwww.R

class CategoryFragment : Fragment() {
    private val mViewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this, CategoryFactory(app = activity!!.application)).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topbook_fragment_category_single, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}

class CategoryViewModel(app: Application) : AndroidViewModel(app) {
    fun requestArticleWithCategoryId(categoryId: String, start: Int, limit: Int) {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getArticleWithCategoryId(categoryId, start = start.toString(), limit = limit.toString())
    }
}

class CategoryFactory(private val app: Application) : ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass::class.java.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(app) as T
        }
        return super.create(modelClass)
    }
}