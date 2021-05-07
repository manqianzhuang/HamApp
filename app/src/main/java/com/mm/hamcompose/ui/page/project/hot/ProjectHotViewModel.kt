package com.mm.hamcompose.ui.page.project.hot

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import com.mm.hamcompose.ui.page.base.IViewContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion

class ProjectHotViewModel @ViewModelInject constructor(
    private var httpRepo: HttpRepo
): BaseViewModel<ParentBean>(), IViewContract.ISubMenuView {

    var tabIndex = MutableLiveData(-1)
    var isRefreshing = MutableLiveData(true)
    var pagingData = MutableLiveData<Flow<PagingData<Article>>>()
    val labelExpand = MutableLiveData(false)
    val selectLabels = mutableListOf<Int>()

    override fun start() {
        if (tabIndex.value==-1) {
            loadCategory()
        }
        refresh()
    }

    fun setTabIndex(index: Int) {
        tabIndex.value = index

        if (isSelected(index))
            return

        //插入已浏览的标签列表
        if (selectLabels.size<4) {
            selectLabels.add(index)
        } else {
            selectLabels.removeAt(3)
            selectLabels.add(0, index)
        }
    }

    //筛选已经选中的标签
    private fun isSelected(index: Int): Boolean {
        selectLabels.forEach {
            if (it==index)
                return true
        }
        return false
    }

    fun setLabelExpand(isExpand: Boolean) {
        labelExpand.value = isExpand
    }

    override fun loadCategory() {
        async {
            httpRepo.getProjectCategory()
                .catch { stopLoading() }
                .onCompletion { stopLoading() }
                .collectLatest { result ->
                    list.value = result.data
                }
        }
    }

    private fun getHotProjects() = httpRepo.getHotProjects()
        .cachedIn(viewModelScope)
        .catch { stopLoading() }
        .onCompletion { stopLoading() }

    fun refresh() {
        pagingData.value = null
        loadContent()
    }

    override fun loadContent() {
        pagingData.value = getHotProjects()
        isRefreshing.value = pagingData.value==null
    }

}
