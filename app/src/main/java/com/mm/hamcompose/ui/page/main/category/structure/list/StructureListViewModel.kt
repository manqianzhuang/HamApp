package com.mm.hamcompose.ui.page.main.category.structure.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingArticle
import com.mm.hamcompose.ui.page.base.BaseCollectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StructureListViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: HistoryDatabase,
): BaseCollectViewModel<Article>(repo) {

    private var cid = -1
    //某个文章的列表
    var articles = MutableLiveData<PagingArticle?>(null)
    var isRefreshing = mutableStateOf(true)
    var authorName = mutableStateOf("")

    fun setId(id: Int) {
        cid = id
    }

    override fun start() {
        initThat { initArticles() }
    }

    fun refresh(author: String) {
        resetListIndex()
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
            articles.value = getStructureArticles()
            isRefreshing.value = articles.value==null
        }
    }

    private fun getStructureArticles() = repo.getStructureArticles(cid).cachedIn(viewModelScope)

    fun searchByAuthor(author: String) {
        isRefreshing.value = true
        articles.value = null
        articles.value = repo.getStructureArticles(author).cachedIn(viewModelScope)
        isRefreshing.value = articles.value==null
    }


    fun saveDataToHistory(article: Article) {
        cacheHistory(db, article)
    }

}