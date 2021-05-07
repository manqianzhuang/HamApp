package com.mm.hamcompose.ui.page.system.tree

import androidx.hilt.lifecycle.ViewModelInject
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion

class SystemViewModel @ViewModelInject constructor(
    private var repo: HttpRepo,
): BaseViewModel<ParentBean>() {

    override fun loadContent() {
        async {
            repo.getSystemList()
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