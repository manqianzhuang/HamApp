package com.mm.hamcompose.ui.page.main.collection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingCollect
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repo: HttpRepository,
): BaseViewModel<ParentBean>() {

    val titles = mutableStateOf(mutableListOf(
        TabTitle(301, "文章列表"),
        TabTitle(302, "我的网址"),
    ))

    var pagingArticleCollect = MutableLiveData<PagingCollect?>(null)
    var webUrlList = mutableStateOf<MutableList<ParentBean>?>(null)
    var uncollectArticleTitle = mutableStateOf<String?>(null)

    override fun start() {
        initThat {
            getCollectUrlList()
            pagingArticleCollect.value = collectList()
        }
    }

    fun refresh() {
        pagingArticleCollect.value = null
        pagingArticleCollect.value = collectList()
    }

    private fun getCollectUrlList() {
        async {
            repo.getCollectUrls().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        webUrlList.value = response.result
                    }
                    is HttpResult.Error -> {

                    }
                }
            }
        }
    }
    private fun collectList() = repo.getCollectionList().cachedIn(viewModelScope)

    fun uncollectArticle(id: Int, originId: Int, title: String) {
        async {
            repo.uncollectArticleById(id, originId).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> { }
                    is HttpResult.Error -> {
                        //收藏接口，不走success判断分支
                        val nullNotice = "the result of remote's request is null"
                        if (response.exception.message==nullNotice) {
                            println("取消收藏(id=$id)")
                            uncollectArticleTitle.value = title
                            refresh()
                        }
                    }
                }
            }
        }
    }

    fun deleteWebsite(id: Int) {
        async {
            repo.deleteWebsite(id).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> { }
                    is HttpResult.Error -> {
                        val nullNotice = "the result of remote's request is null"
                        if (response.exception.message==nullNotice) {
                            getCollectUrlList()
                        }
                    }
                }
            }
        }
    }

}