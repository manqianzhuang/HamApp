package com.mm.hamcompose.ui.page.subscription.category

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.*
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.*

class SubscriptionViewModel @ViewModelInject constructor(
    private var httpRepo: HttpRepo
): BaseViewModel<ParentBean>() {

    //公众号ID
    private var publicId = MutableLiveData(-1)


    override fun start() {
        if (publicId.value==-1) {
            loadContent()
        }
    }

    override fun loadContent() {
        async {
            httpRepo.getPublicInformation()
                .catch { stopLoading() }
                .onCompletion { stopLoading() }
                .collect { result ->
                    list.value = result.data
                }
        }
    }


}