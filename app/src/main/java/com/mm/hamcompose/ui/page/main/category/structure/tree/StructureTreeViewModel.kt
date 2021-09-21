package com.mm.hamcompose.ui.page.main.category.structure.tree

import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class StructureViewModel @Inject constructor(
    private var repo: HttpRepository,
): BaseViewModel<ParentBean>() {

    override fun loadContent() {
        async {
            repo.getStructureList().collectLatest { response ->
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