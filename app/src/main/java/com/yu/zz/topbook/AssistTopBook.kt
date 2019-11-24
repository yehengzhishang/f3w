package com.yu.zz.topbook

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yu.zz.common.arrange.goToThreadMain
import com.yu.zz.fwww.R
import com.yu.zz.topbook.category.CategorySingleFragment
import com.yu.zz.topbook.category.KEY_CATEGORY_ID
import com.yu.zz.topbook.deep.CategoryResponseTopBookBean
import com.yu.zz.topbook.deep.CategoryTopBookBean
import com.yu.zz.topbook.deep.TopBookApi
import com.yu.zz.topbook.deep.TopBookService
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.topbook_activity_assist.*
import androidx.lifecycle.Observer as OB

class AssistTopBookActivity : AppCompatActivity() {
    private val mViewModel: AssistViewModel by lazy {
        ViewModelProviders.of(this, AssistViewModelFactory(application)).get(AssistViewModel::class.java)
    }
    private val mAdapter: CategoryAdapter by lazy {
        CategoryAdapter(supportFragmentManager, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topbook_activity_assist)
        vp.adapter = mAdapter
        mViewModel.getDataCategory().observe(this, OB {
            if (it != null) {
                val list = mutableListOf<CategoryTopBookBean>()
                for (bean in it) {
                    bean.name ?: continue
                    list.add(bean)
                }
                mAdapter.add(it)
                TabLayoutMediator(tl, vp) { tab, pos ->
                    tab.tag = pos
                    tab.text = list[pos].name
                }.attach()
                mAdapter.notifyDataSetChanged()
            }
        })
        mViewModel.requestCategoryList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topboob_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change -> changeMain()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeMain(): Boolean {
        startActivity(Intent(this, MainTopBookActivity::class.java))
        finish()
        return true
    }
}

class CategoryAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val mListArticle = mutableListOf<CategoryTopBookBean>()
    fun add(list: List<CategoryTopBookBean>) {
        mListArticle.addAll(list)
    }

    override fun getItemCount(): Int = mListArticle.size

    override fun createFragment(position: Int): Fragment = CategorySingleFragment().apply {
        arguments = Bundle().apply {
            putString(KEY_CATEGORY_ID, mListArticle[position].categoryId!!.toString())
        }
    }
}

class AssistViewModel(app: Application) : AndroidViewModel(app) {
    private val mDataCategory: MutableLiveData<List<CategoryTopBookBean>> by lazy {
        MutableLiveData<List<CategoryTopBookBean>>()
    }

    fun getDataCategory(): LiveData<List<CategoryTopBookBean>> = mDataCategory
    fun requestCategoryList() {
        TopBookApi.INSTANCE.retrofit.create(TopBookService::class.java)
                .getListCategory(start = "0", limit = "20")
                .filter { it.isSuccess() && it.data != null }
                .goToThreadMain()
                .subscribe(object : Observer<CategoryResponseTopBookBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: CategoryResponseTopBookBean) {
                        mDataCategory.value = t.data!!.getList()
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }
}

class AssistViewModelFactory(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssistViewModel::class.java)) {
            return AssistViewModel(app) as T
        }
        return super.create(modelClass)
    }
}