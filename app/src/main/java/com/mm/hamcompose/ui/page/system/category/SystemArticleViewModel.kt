package com.mm.hamcompose.ui.page.system.category

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion

class SystemArticleViewModel @ViewModelInject constructor(
    private val repo: HttpRepo
): BaseViewModel<Article>() {

    private var cid = -1

    fun setId(id: Int) {
        cid = id
    }

    override fun start() {
        refresh("")
    }

    /**
     * 某个文章的列表
     */
    var articles = MutableLiveData<Flow<PagingData<Article>>>()
    var isRefreshing = MutableLiveData(true)


    fun refresh(author: String) {
        isRefreshing.value = true
        articles.value = null
        if (author.isEmpty()) {
            initArticles()
        } else {
            searchByAuthor(author)
        }
    }

    fun initArticles() {
        if (articles.value==null) {
            articles.value = getSystemArticles()
            isRefreshing.value = articles.value==null
        }
    }

    private fun getSystemArticles() = repo.getSystemArticles(cid).cachedIn(viewModelScope)

    fun searchByAuthor(author: String) {
        isRefreshing.value = true
        articles.value = null
        articles.value = repo.getSystemArticles(author).cachedIn(viewModelScope)
        isRefreshing.value = articles.value==null
    }
}