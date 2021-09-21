package com.mm.hamcompose.ui.page.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {

    //分类列表（装非分页加载的容器）
    var list = mutableStateOf(mutableListOf<T>())

    var currentListIndex = mutableStateOf(0)

    private var _isInited = mutableStateOf(false)

    private val isInited: Boolean
        get() = _isInited.value

    private fun requestInitialized() {
        _isInited.value = true
    }

    fun resetListIndex() {
        currentListIndex.value = 0
    }

    fun resetInitState() {
        _isInited.value = false
    }

    fun async(block: suspend ()-> Unit) {
        viewModelScope.launch { block() }
    }

    abstract fun start()

    fun initThat(block: () -> Unit) {
        if (!isInited) {
            block.invoke()
            requestInitialized()
        }
    }

    fun savePosition(index: Int) {
        currentListIndex.value = index
        println("## save position = $index ##")
    }

    open fun stopLoading() { }

    open fun loadContent() { }

}