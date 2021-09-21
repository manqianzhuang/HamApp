package com.mm.hamcompose.ui.page.main.category.navigation

import com.mm.hamcompose.data.bean.NaviWrapper
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class NaviViewModel @Inject constructor(
    private val httpRepo: HttpRepository
): BaseViewModel<NaviWrapper>() {

    override fun loadContent() {
        async {
            httpRepo.getNavigationList().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        list.value = response.result
                    }
                    is HttpResult.Error -> {

                    }
                }
            }
        }
    }

    override fun start() {
        initThat { loadContent() }
    }


}