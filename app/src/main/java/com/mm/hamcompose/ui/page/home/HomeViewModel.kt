package com.mm.hamcompose.ui.page.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import com.mm.hamcompose.bean.HomeThemeBean
import com.mm.hamcompose.ui.page.base.BaseViewModel
import com.mm.hamcompose.theme.HamTheme

class HomeViewModel @ViewModelInject constructor(): BaseViewModel<HomeThemeBean>() {

    var theme by mutableStateOf(HamTheme.Theme.Light)

    override fun loadContent() {

    }

    override fun start() {

    }




}