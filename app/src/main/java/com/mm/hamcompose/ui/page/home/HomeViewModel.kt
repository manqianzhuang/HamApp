package com.mm.hamcompose.ui.page.home

import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.HomeThemeBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.page.base.BaseViewModel

class HomeViewModel @ViewModelInject constructor(): BaseViewModel<HomeThemeBean>() {

    var theme = mutableStateOf(HamTheme.Theme.Light)
    var menuItems = mutableListOf(
        MenuAttr("主页", null),
        MenuAttr("福利", R.drawable.ic_menu_welfare),
        MenuAttr("收藏", R.drawable.ic_star),
        MenuAttr("设置", R.drawable.ic_menu_settings),
    )

    override fun loadContent() {

    }

    override fun start() {

    }

    data class MenuAttr(
        val title: String,
        val iconRes: Int?
    )



}