package com.mm.hamcompose.ui.page.project.category

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import com.mm.hamcompose.ui.page.base.IViewContract
import com.mm.hamcompose.ui.page.base.RefreshViewModel
import kotlinx.coroutines.flow.*

class ProjectCategoryViewModel @ViewModelInject constructor(
    private var httpRepo: HttpRepo
): BaseViewModel<Article>() {

    private var cId = MutableLiveData(-1)
    var isRefreshing = MutableLiveData(true)
    var pagingData = MutableLiveData<Flow<PagingData<Article>>>()

    override fun start() {

    }

    fun setCid(id: Int) {
        cId.value = id
    }

    private fun getProjects() = httpRepo.getProjectData(cId.value!!)
            .cachedIn(viewModelScope)
            .catch { stopLoading() }
            .onCompletion { stopLoading() }

    private fun initProjectData() {
        if (pagingData.value==null) {
            pagingData.value = getProjects()
            isRefreshing.value = pagingData.value==null
        }
    }

    fun refresh() {
        pagingData.value = null
        initProjectData()
    }

    override fun loadContent() {
//        async {
//            httpRepo.getProjects(page.value!!, cId.value!!)
//                .catch { stopLoading()  }
//                .onCompletion { stopLoading() }
//                .collect { result ->
//                    setRefreshData(result.data)
//                }
//        }
    }

}