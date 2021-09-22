package com.mm.hamcompose.ui.page.main.home.square

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
class SquareViewModel @Inject constructor(
    private var repo: HttpRepository,
    private val db: HistoryDatabase,
): BaseCollectViewModel<Article>(repo) {

    var pagingData = MutableLiveData<PagingArticle?>(null)
    var isRefreshing = mutableStateOf(false)

    override fun start() {
        initThat {
            pagingData.value = squareData()
        }
    }

    fun refresh() {
        resetListIndex()
        isRefreshing.value = true
        pagingData.value = null
        pagingData.value = squareData()
        isRefreshing.value = pagingData.value==null
    }

    private fun squareData() = repo.getSquareData().cachedIn(viewModelScope)

    fun saveDataToHistory(article: Article) {
        cacheHistory(db, article)
    }

    override fun onCleared() {
        LogUtils.e("SquareViewModel ===> ViewModel执行onCleared()")
        super.onCleared()
    }


}