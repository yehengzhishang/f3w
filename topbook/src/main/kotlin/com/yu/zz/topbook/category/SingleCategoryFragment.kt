package com.yu.zz.topbook.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yu.zz.bypass.FlyNetConfig
import com.yu.zz.bypass.ServiceFactory
import com.yu.zz.bypass.app.getAppConfig
import com.yu.zz.bypass.getFactoryDefaultCall
import com.yu.zz.bypass.getFactoryDefaultConverter
import com.yu.zz.topbook.databinding.TopbookFragmentCategorySingleBinding
import com.yu.zz.topbook.employ.ArticleResponseTopBookBean
import com.yu.zz.topbook.employ.TOPBOOK_PATH_CATEGORY_ID
import com.yu.zz.topbook.employ.TOPBOOK_URL_BASE
import com.yu.zz.topbook.employ.TOPBOOK_URL_PATH_PREFIX
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

@AndroidEntryPoint
class SingleCategoryFragment : Fragment() {
    private lateinit var mBinding: TopbookFragmentCategorySingleBinding

    private val mRv: RecyclerView by lazy { mBinding.rv }

    private val mSrl: SwipeRefreshLayout by lazy { mBinding.srl }

    private val mViewModel: SingleCategoryViewModel by viewModels()

    private val mAdapter: CategorySingleAdapter = CategorySingleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TopbookFragmentCategorySingleBinding.inflate(inflater, container, false)
        mSrl.isEnabled = false
        mRv.adapter = mAdapter
        val context = requireContext()
        mRv.layoutManager = GridLayoutManager(context, 2)
        mRv.itemAnimator = DefaultItemAnimator().apply {
            addDuration = 2000

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.livedataList.observe(this.viewLifecycleOwner, Observer {
            mAdapter.addBean(it.data!!.getList())
            mAdapter.notifyItemRangeInserted(0, it.data!!.getList().size)
        })
        mViewModel.loadById("24")
    }
}

@HiltViewModel
class SingleCategoryViewModel @Inject constructor(private val service: CategoryService) :
    ViewModel() {
    private val mLivedataList: MutableLiveData<ArticleResponseTopBookBean> by lazy {
        MutableLiveData<ArticleResponseTopBookBean>()
    }
    val livedataList: LiveData<ArticleResponseTopBookBean> get() = mLivedataList
    fun loadById(categoryId: String) {
        viewModelScope.launch {
            mLivedataList.value =
                service.getArticlesByCategoryId(categoryId, start = "0", limit = "20")
        }
    }
}

interface CategoryService {
    @GET(TOPBOOK_URL_PATH_PREFIX)
    suspend fun getArticlesByCategoryId(
        @Path(TOPBOOK_PATH_CATEGORY_ID) categoryId: String,
        @Query("start") start: String,
        @Query("limit") limit: String
    ): ArticleResponseTopBookBean
}

class SimplicityNet(private val config: FlyNetConfig) : ServiceFactory {
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(config.getClient())
        .baseUrl(config.baseUrl)
        .addConverterFactory(config.converterFactory)
        .build()

    override fun <T> createService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SimplicityApiModule {
    @Binds
    abstract fun bindApi(api: SimplicityApi): ServiceFactory
}

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideCategoryService(factory: ServiceFactory): CategoryService {
        return factory.createService(CategoryService::class.java)
    }

    @Provides
    fun provideApi(): SimplicityApi {
        return SimplicityApi.INSTANCE
    }
}

class SimplicityApi(factory: ServiceFactory) : ServiceFactory by factory {
    companion object {
        val INSTANCE = Holder.api
    }

    private object Holder {
        val api: SimplicityApi = SimplicityApi(
            SimplicityNet(
                FlyNetConfig(
                    baseUrl = TOPBOOK_URL_BASE,
                    callFactory = getFactoryDefaultCall(),
                    converterFactory = getFactoryDefaultConverter(),
                    isDebug = getAppConfig().isDebug
                )
            )
        )
    }
}