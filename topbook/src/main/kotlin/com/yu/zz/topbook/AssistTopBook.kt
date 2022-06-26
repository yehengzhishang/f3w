package com.yu.zz.topbook

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yu.zz.bypass.goToThreadMain
import com.yu.zz.topbook.category.CategorySingleFragment
import com.yu.zz.topbook.category.KEY_CATEGORY_ID
import com.yu.zz.topbook.employ.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single

@AndroidEntryPoint
class AssistTopBookActivity : TopBookActivity() {
    private val mViewModel: AssistViewModel by lazy {
        createViewModel<AssistViewModel>(
            factory = AssistViewModelFactory(
                application,
                AssistRepository(createService())
            )
        )
    }
    private val mAdapter: CategoryAdapter by lazy {
        CategoryAdapter(supportFragmentManager, lifecycle)
    }

    private fun obData(result: List<CategoryTopBookBean>?) {
        if (result == null) {
            return
        }
        val list = mutableListOf<CategoryTopBookBean>()
        for (bean in result) {
            bean.name ?: continue
            list.add(bean)
        }
        mAdapter.add(result)
        TabLayoutMediator(findViewById(R.id.tl), findViewById(R.id.vp)) { tab, pos ->
            tab.tag = pos
            tab.text = list[pos].name
        }.attach()
        mAdapter.notifyDataSetChanged()
    }

    private fun changeMain(): Boolean {
        startActivity(Intent(this, FoundationTopBookActivity::class.java))
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
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

    override fun layoutId(): Int {
        return R.layout.topbook_activity_assist
    }

    override fun createFirstInit() {
        super.createFirstInit()
        setSupportActionBar(findViewById(R.id.tb))
    }

    override fun createSecondUi() {
        findViewById<ViewPager2>(R.id.vp).adapter = mAdapter
    }

    override fun createThirdData() {
        mViewModel.dataCategory.observe(this, Observer { result: List<CategoryTopBookBean>? ->
            this.obData(result)
        })
        mViewModel.requestCategoryList()
    }
}

class CategoryAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
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

class AssistViewModelFactory(private val app: Application, private val repo: AssistRepository) :
    ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssistViewModel::class.java)) {
            return AssistViewModel(app, repo) as T
        }
        return super.create(modelClass)
    }
}

class AssistViewModel(app: Application, private val repo: AssistRepository) :
    TopBookViewModel(app) {
    private val mDataCategory: MutableLiveData<List<CategoryTopBookBean>> by lazy {
        MutableLiveData<List<CategoryTopBookBean>>()
    }
    val dataCategory: LiveData<List<CategoryTopBookBean>> get() = mDataCategory

    fun requestCategoryList() {
        repo.getListCategory(start = "0", limit = "20")
            .filter { it.isSuccess() && it.data != null }
            .goToThreadMain()
            .subscribe(getMaybe { bean -> mDataCategory.value = bean.data!!.getList() })
    }
}

class AssistRepository(private val service: SingleTopBookService) {
    fun getListCategory(start: String, limit: String): Single<CategoryResponseTopBookBean> {
        return service.getListCategory(start, limit)
    }
}
