package com.mm.hamcompose.ui.page.main.home.wenda

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingArticle
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WenDaViewModel @Inject constructor(
    private var repo: HttpRepository
): BaseViewModel<Article>() {

    var pagingData = MutableLiveData<PagingArticle?>(null)
    var isRefreshing = mutableStateOf(false)

    override fun start() {
        initThat { pagingData.value = squareData() }
    }

    fun refresh() {
        resetListIndex()
        isRefreshing.value = true
        pagingData.value = null
        pagingData.value = squareData()
        isRefreshing.value = pagingData.value==null
    }

    private fun squareData() = repo.getWendaData().cachedIn(viewModelScope)

}