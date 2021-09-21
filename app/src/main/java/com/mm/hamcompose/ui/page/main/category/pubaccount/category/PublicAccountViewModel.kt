package com.mm.hamcompose.ui.page.main.category.pubaccount.category

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class PublicAccountViewModel @Inject constructor(
    private var httpRepo: HttpRepository
): BaseViewModel<ParentBean>() {

    //公众号ID
    private var publicId = mutableStateOf(-1)

    override fun start() {
        initThat { loadContent() }
    }

    override fun loadContent() {
        async {
            httpRepo.getPublicInformation().collectLatest { response ->
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


}