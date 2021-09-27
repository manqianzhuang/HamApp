package com.mm.hamcompose.ui.page.main.category

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor() : BaseViewModel<ParentBean>() {

    val titles = mutableStateOf(
        mutableListOf(
            TabTitle(201, "体系"),
            TabTitle(202, "导航"),
            TabTitle(203, "公众号"),
        )
    )


    override fun start() {

    }


    override fun onCleared() {
        super.onCleared()
        println("CategoryViewModel ==> onClear")
    }

}