package com.mm.hamcompose.ui.page.main.category.pubaccount.search

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
class PublicAccountSearchViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: HistoryDatabase,
): BaseCollectViewModel<Article>(repo) {

    /**
     * 在某个公众号下，搜索关键字
     */
    //var searchText = MutableLiveData("")
    var isRefreshing = mutableStateOf(false)
    var searchContent = mutableStateOf("")
    val searchResult = MutableLiveData<PagingArticle?>(null)
    private var publicId = mutableStateOf(-1)


    override fun start() {

    }

    fun setPublicId(id: Int) {
        this.publicId.value = id
    }

    private fun searchArticleWithKey(key: String) =
        repo.searchArticleWithKey(publicId.value, key).cachedIn(viewModelScope)

    fun refreshSearch(key: String) {
        resetListIndex()
        search(key)
    }

    fun search(key: String) {
        searchResult.value = null
        searchResult.value = searchArticleWithKey(key)
        isRefreshing.value = searchResult.value==null
    }

    fun saveDataToHistory(article: Article) {
        cacheHistory(db, article)
    }


}