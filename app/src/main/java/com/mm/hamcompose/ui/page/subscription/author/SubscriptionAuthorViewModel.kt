package com.mm.hamcompose.ui.page.subscription.author

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

class SubscriptionAuthorViewModel @ViewModelInject constructor(
    private var repo: HttpRepo
): BaseViewModel<Article>() {

    private var publicId = MutableLiveData(-1)

    override fun start() {
        refresh()
    }

    fun setPublicId(id: Int) {
        publicId.value = id
    }

    /**
     * 某个技术公众号的列表
     */
    var publicData = MutableLiveData<Flow<PagingData<Article>>>()
    var isRefreshing = MutableLiveData(true)


    fun refresh() {
        isRefreshing.value = true
        publicData.value = null
        initPublicArticles()
    }

    fun initPublicArticles() {
        if (publicData.value==null) {
            publicData.value = getPublicArticles()
            isRefreshing.value = publicData.value==null
        }
    }

    private fun getPublicArticles() = repo.getPublicArticles(publicId.value!!)
        .cachedIn(viewModelScope)
        .catch { stopLoading() }
        .onCompletion { stopLoading() }

}