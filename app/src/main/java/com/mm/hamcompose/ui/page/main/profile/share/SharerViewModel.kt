package com.mm.hamcompose.ui.page.main.profile.share

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.MY_USER_ID
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SharerViewModel @Inject constructor(private val repo: HttpRepository): BaseViewModel<Article>() {

    private var page = mutableStateOf(1)
    var points = mutableStateOf<PointsBean?>(null)
    var articles = mutableStateListOf<Article>()
    private val userId = mutableStateOf(-1)
    var isLoadingMore = mutableStateOf(false)
    var hasMore = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun setupUserId(id: Int) {
        this.userId.value = id
    }

    override fun start() {
        initThat {
            getShareData()
        }
    }

    fun nextPage() {
        page.value += 1
        getShareData()
    }

    private fun getShareData() {
        async {
            isLoadingMore.value = true
            val call = if (userId.value == MY_USER_ID) {
                repo.getMyShareArticles(page.value)
            } else {
                repo.getAuthorShareArticles(userId.value, page.value)
            }

            call.collectLatest { response ->
                isLoadingMore.value = false
                when (response) {
                    is HttpResult.Success -> {
                        points.value = response.result.coinInfo
                        articles.addAll(response.result.shareArticles.datas)
                        hasMore.value = !response.result.shareArticles.over
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                        errorMessage.value = response.exception.message
                    }
                }
            }
        }
    }

    fun deleteMyArticle(id: Int) {
        async {
            repo.deleteMyShareArticle(id).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                        val error = response.exception.message
                        if (error == "the result of remote's request is null") {
                            val deleteItem = articles.find { it.id == id }
                            articles.remove(deleteItem)
                        }
                    }
                }
            }
        }
    }

}