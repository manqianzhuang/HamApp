package com.mm.hamcompose.ui.page.main.collection.edit

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class WebSiteEditViewModel @Inject constructor(
    private val repo: HttpRepository
): BaseViewModel<Any>() {

    var webSiteTitle = mutableStateOf<String?>(null)
    var linkUrl = mutableStateOf<String?>(null)
    var author = mutableStateOf<String?>(null)
    var isSaved = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    val titles = mutableStateOf(mutableListOf(
        TabTitle(601, "网站"),
        TabTitle(602, "文章"),
    ))

    override fun start() {

    }

    /**
     * type: 0 = 添加新网站， 1 = 添加新文章 ， -1 = 编辑网站
     * id: 仅编辑网站时候用到此参数
     */
    fun saveNewCollect(type: Int, id: Int = 0) {
        async {
            when (type) {
                0 -> {
                    repo.addNewWebsiteCollect(webSiteTitle.value!!, linkUrl.value!!)
                        .collectLatest { response ->
                            when (response) {
                                is HttpResult.Success -> {
                                    isSaved.value = true
                                }
                                is HttpResult.Error -> {
                                    println(response.exception.message)
                                    errorMessage.value = response.exception.message ?: "请求异常"
                                }
                            }
                        }
                }
                1 -> {
                    repo.addNewArticleCollect(webSiteTitle.value!!, linkUrl.value!!, author.value!!)
                        .collectLatest { response ->
                            when (response) {
                                is HttpResult.Success -> {
                                    isSaved.value = true
                                }
                                is HttpResult.Error -> {
                                    println(response.exception.message)
                                    errorMessage.value = response.exception.message ?: "请求异常"
                                }
                            }
                        }
                }
                else -> {
                    repo.editCollectWebsite(id, webSiteTitle.value!!, linkUrl.value!!)
                        .collectLatest { response ->
                            when (response) {
                                is HttpResult.Success -> {
                                    isSaved.value = true
                                }
                                is HttpResult.Error -> {
                                    println(response.exception.message)
                                    errorMessage.value = response.exception.message ?: "请求异常"
                                }
                            }
                        }
                }
            }
        }
    }

    private fun setResponseState(errorInfo: String?) {
        isSaved.value = errorInfo == "the result of remote's request is null"
    }

}