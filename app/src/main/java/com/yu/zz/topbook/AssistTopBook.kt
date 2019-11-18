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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yu.zz.common.arrange.goToThreadMain
import com.yu.zz.fwww.R
import com.yu.zz.topbook.category.CategorySingleFragment
import com.yu.zz.topbook.category.KEY_CATEGORY_ID
import com.yu.zz.topbook.deep.*
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
    private val tabChange: TabLayout.OnTabSelectedListener =
            object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    vp.setCurrentItem(tab.position, false)
                }
            }
    private val pageChange: ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tl.setScrollPosition(position, 0F, true)
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topbook_activity_assist)
        vp.adapter = mAdapter
        tl.addOnTabSelectedListener(tabChange)
        vp.registerOnPageChangeCallback(pageChange)
        mViewModel.getDataCategory().observe(this, OB {
            tl.removeAllTabs()
            if (it != null) {
                for (bean in it) {
                    val title = bean.name ?: continue
                    val tab = tl.newTab()
                    tab.tag = it.indexOf(bean)
                    tab.text = title
                    tl.addTab(tab)
                }
                mAdapter.add(it)
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