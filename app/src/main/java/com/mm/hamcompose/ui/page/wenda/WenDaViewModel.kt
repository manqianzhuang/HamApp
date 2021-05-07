package com.mm.hamcompose.ui.page.wenda

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class WenDaViewModel @ViewModelInject constructor(
    private var repo: HttpRepo
): BaseViewModel<Article>() {

    var pagingData = MutableLiveData<Flow<PagingData<Article>>>()
    var isRefreshing = MutableLiveData(true)

    override fun start() {
        if (pagingData.value==null) {
            pagingData.value = squareData()
            isRefreshing.value = pagingData.value==null
        }
    }

    fun refresh() {
        isRefreshing.value = true
        pagingData.value = null
        start()
    }

    private fun squareData() = repo.getWendaData()

}