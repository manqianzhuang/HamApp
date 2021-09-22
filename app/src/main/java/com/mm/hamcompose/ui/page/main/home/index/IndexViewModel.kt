package com.mm.hamcompose.ui.page.main.home.index

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.BannerBean
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingArticle
import com.mm.hamcompose.ui.page.base.BaseCollectViewModel
import com.mm.hamcompose.ui.widget.BannerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@HiltViewModel
class IndexViewModel @Inject constructor(
    private var repo: HttpRepository,
    private val historyDb: HistoryDatabase
) : BaseCollectViewModel<BannerBean>(repo) {

    var pagingData = MutableLiveData<PagingArticle>(null)
    val imageList = mutableStateOf(mutableListOf<BannerData>())
    var isRefreshing = mutableStateOf(false)
    var topArticles = mutableStateListOf<Article>()

    //列表：使用paging3分页加载框架
    private fun homeData() = repo.getIndexData().cachedIn(viewModelScope)

    private fun loadBanners() {
        async {
            repo.getBanners().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        imageList.value = response.result.map {
                            BannerData(
                                imageUrl = it.imagePath ?: "",
                                linkUrl = it.url ?: "",
                                title = it.title ?: ""
                            )
                        } as MutableList<BannerData>
                    }
                    is HttpResult.Error -> {
                        imageList.value.clear()
                    }
                }
            }
        }
    }

    private fun loadTopArticles() {
        async {
            repo.getTopArticles().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        topArticles.clear()
                        topArticles.addAll(response.result)
                    }
                    is HttpResult.Error -> {
                        topArticles.clear()
                    }
                }
            }
        }
    }

    private fun refreshBanner() {
        imageList.value.clear()
        loadBanners()
    }

    private fun refreshHots() {
        topArticles.clear()
        loadTopArticles()
    }

    private fun getHomesList() {
        pagingData.value = null
        pagingData.value = homeData()
        isRefreshing.value = pagingData.value == null
    }

    fun refresh() {
        resetListIndex()
        isRefreshing.value = true
        refreshBanner()
        refreshHots()
        getHomesList()
    }

    override fun start() {
        initThat {
            loadBanners()
            loadTopArticles()
            getHomesList()
        }
    }

    override fun onCleared() {
        LogUtils.e("IndexViewModel ===> ViewModel执行onCleared()")
        super.onCleared()
    }

    fun saveDataToHistory(article: Article) {
        cacheHistory(historyDb, article)
    }

}