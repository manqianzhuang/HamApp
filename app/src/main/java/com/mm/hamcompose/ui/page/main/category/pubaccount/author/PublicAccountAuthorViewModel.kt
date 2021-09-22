package com.mm.hamcompose.ui.page.main.category.pubaccount.author

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingArticle
import com.mm.hamcompose.ui.page.base.BaseCollectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PublicAccountAuthorViewModel @Inject constructor(
    private var repo: HttpRepository,
    private val db: HistoryDatabase,
): BaseCollectViewModel<Article>(repo) {

    /**
     * 某个技术公众号的列表
     */
    var publicData = MutableLiveData<PagingArticle?>(null)
    var isRefreshing = mutableStateOf(false)
    private var authorId = mutableStateOf(-1)

    override fun start() {
        initThat { initPublicArticles() }
    }

    fun setPublicId(id: Int) {
        authorId.value = id
    }

    fun clearCache() {
        isRefreshing.value = true
        publicData.value = null
    }

    fun initPublicArticles() {
        if (publicData.value==null) {
            publicData.value = getPublicArticles()
            isRefreshing.value = publicData.value==null
        }
    }

    private fun getPublicArticles() = repo.getPublicArticles(authorId.value).cachedIn(viewModelScope)

    override fun onCleared() {
        LogUtils.e("ViewModel执行onCleared()")
        super.onCleared()
    }

    fun saveDataToHistory(article: Article) {
        cacheHistory(db, article)
    }


}