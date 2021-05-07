package com.mm.hamcompose.ui.page.base

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.ListWrapper

abstract class RefreshViewModel<T, R>: BaseViewModel<T>() {

    val refreshList = MutableLiveData<ListWrapper<R>>()
    var noMoreData = MutableLiveData(false)
    var page = MutableLiveData(0)
    private var total = 0

    fun refresh() {
        reset()
        list.value!!.clear()
        loadAllData()
    }

    fun loadMore() {
        page.value = page.value!!.plus(1)
        if (refreshList.value!!.datas.size>=total) {
            noMoreData.value = true
        } else {
            LogUtils.e("加载更多")
            loadContent()
        }
    }

    abstract fun loadAllData()

    fun setRefreshData(list: ListWrapper<R>) {
        if (refreshList.value==null) {
            refreshList.value = list
        } else {
            LogUtils.e("插入数据")
            refreshList.value!!.datas.addAll(list.datas)
        }
        setTotal(list.total)
    }

    private fun setTotal(total: Int) {
        this.total = total
        noMoreData.value = total<=refreshList.value!!.datas.size
    }

    fun reset() {
        page.value = 0
        this.total = 0
        this.refreshList.value?.datas?.clear()
        noMoreData.value = false
    }
}