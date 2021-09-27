package com.mm.hamcompose.ui.page.main.home

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.HomeThemeBean
import com.mm.hamcompose.data.bean.MenuTitle
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeThemeBean>() {

    var theme = mutableStateOf(HamTheme.Theme.Light)
    var menuItems = mutableListOf(
        MenuTitle("主页", null),
        MenuTitle("福利", R.drawable.ic_menu_welfare),
        MenuTitle("收藏", R.drawable.ic_star),
        MenuTitle("设置", R.drawable.ic_menu_settings),
    )

    var isShowSearchBar = mutableStateOf(true)
    val titles = mutableStateOf(
        mutableListOf(
            TabTitle(101, "推荐"),
            TabTitle(102, "广场"),
            TabTitle(103, "项目"),
            TabTitle(104, "问答")
        )
    )

    fun setCachePosition(tabIndex: Int, newPosition: Int) {
        titles.value[tabIndex].cachePosition = newPosition
        //val oldPosition = titles.value[tabIndex].cachePosition
        //isShowSearchBar.value = newPosition <= 1
        //LogUtils.w("newPosition = $newPosition isShowSearch = ${isShowSearchBar.value}")
    }

    override fun start() {

    }

    override fun onCleared() {
        super.onCleared()
        println("HomeViewModel ==> onClear")
    }

}