package com.mm.hamcompose.ui.page.index

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.BannerBean
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import com.mm.hamcompose.ui.page.base.IViewContract
import com.mm.hamcompose.ui.page.base.RefreshViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class IndexViewModel @ViewModelInject constructor(
    private var repo: HttpRepo,
): BaseViewModel<BannerBean>(), IViewContract.IHomeView  {

    var pagingData = MutableLiveData<Flow<PagingData<Article>>>()
    var images = MutableLiveData(mutableListOf<String>())
    var isRefreshing = MutableLiveData(true)
    val topArticles = MutableLiveData(mutableListOf<Article>())

    //列表：使用paging3分页加载框架
    private fun homeData() =
        repo.getIndexData()
            .cachedIn(viewModelScope)
            .catch { stopLoading() }
            .onCompletion {
                stopLoading()
            }
            //.asLiveData()

    //列表：普通分页加载
//    override fun loadContent() {
//        async {
//            repo.getIndexList(page.value)
//                .catch { stopLoading() }
//                .onCompletion { stopLoading() }
//                .collect { result ->
//                    LogUtils.w("当前线程： ${Thread.currentThread().name}")
//                    if (!noMoreData.value) {
//                        setRefreshData(result.data)
//                    }
//                }
//        }
//    }

    override fun loadBanners() {
        images.value?.clear()
        async {
            repo.getBanners()
                .map { bean ->
                    bean.data.map {
                        LogUtils.w("banner图片 ${it.imagePath}")
                        it.imagePath?: ""
                    }
                }.collect { images.value = it as MutableList<String> }
        }
    }

    override fun loadTopArticles() {
        async {
            repo.getTopArticles()
                .collectLatest {
                    topArticles.value = it.data
                }
        }
    }


    override fun start() {
        if (pagingData.value==null) {
            pagingData.value = homeData()
            isRefreshing.value = pagingData.value==null
        }
        if (topArticles.value!!.isEmpty()) {
            loadTopArticles()
        }
        //loadAllData()
    }

    override fun onCleared() {
        LogUtils.e("ViewModel执行onCleared()")
        super.onCleared()
    }

}