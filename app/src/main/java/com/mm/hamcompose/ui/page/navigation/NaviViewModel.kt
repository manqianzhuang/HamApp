package com.mm.hamcompose.ui.page.navigation

import androidx.hilt.lifecycle.ViewModelInject
import com.mm.hamcompose.bean.NaviWrapper
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion

class NaviViewModel @ViewModelInject constructor(
    private val httpRepo: HttpRepo
): BaseViewModel<NaviWrapper>() {

    override fun loadContent() {
        async {
            httpRepo.getNavigationList()
                .catch { stopLoading() }
                .onCompletion { stopLoading() }
                .collect { result ->
                    list.value = result.data
                }
        }
    }

    override fun start() {
        if (list.value!!.isEmpty()) {
            loadContent()
        }
    }

}