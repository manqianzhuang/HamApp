package com.mm.hamcompose.ui.page.subscription.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion

class SubscriptionSearchViewModel @ViewModelInject constructor(
    val httpRepo: HttpRepo
): BaseViewModel<Article>() {

    /**
     * 在某个公众号下，搜索关键字
     */
    //var searchText = MutableLiveData("")
    var isRefreshing = MutableLiveData(false)
    val searchResult = MutableLiveData<Flow<PagingData<Article>>>()
    private var publicId = MutableLiveData(-1)


    override fun start() {
        searchResult.value = null
    }

    fun setPublicId(id: Int) {
        this.publicId.value = id
    }

    private fun searchArticleWithKey(key: String) =
        httpRepo.searchArticleWithKey(publicId.value!!, key)
            .cachedIn(viewModelScope)
            .catch { stopLoading() }
            .onCompletion { stopLoading() }

    fun refreshSearch(key: String) {
        search(key)
    }

    fun search(key: String) {
        searchResult.value = null
        searchResult.value = searchArticleWithKey(key)
        isRefreshing.value = searchResult.value==null
    }

}