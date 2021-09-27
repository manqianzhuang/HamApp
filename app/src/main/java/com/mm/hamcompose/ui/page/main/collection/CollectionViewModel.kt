package com.mm.hamcompose.ui.page.main.collection

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingCollect
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: UserInfoDatabase
) : BaseViewModel<ParentBean>() {

    val titles = mutableStateOf(
        mutableListOf(
            TabTitle(301, "文章列表"),
            TabTitle(302, "我的网址"),
        )
    )

    var collectArticles = MutableLiveData<PagingCollect?>(null)
    var webUrlList = mutableStateOf<MutableList<ParentBean>?>(null)
    var isRefreshing = mutableStateOf(false)
    var isLogin = mutableStateOf(false)

    override fun start() {
        checkLoginState()
    }

    private fun checkLoginState() {
        async {
            flow { emit(db.userInfoDao().queryUserInfo()) }
                .flowOn(Dispatchers.IO)
                .collectLatest { users ->
                    isLogin.value = users.isNotEmpty()
                    if (isLogin.value && isNotInit()) {
                        initData()
                    }
                }
        }
    }

    private fun isNotInit() = collectArticles.value == null && webUrlList.value == null

    private fun initData() {
        getCollectUrlList()
        getArticles()
    }

    fun refresh() {
        isRefreshing.value = true
        webUrlList.value = null
        collectArticles.value = null
        checkLoginState()
    }

    private fun getArticles() {
        collectArticles.value = collectList()
        isRefreshing.value = collectArticles.value == null
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

    fun uncollectArticle(id: Int, originId: Int) {
        async {
            repo.uncollectArticleById(id, originId).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                    }
                    is HttpResult.Error -> {
                        //收藏接口，不走success判断分支
                        val deleted = response.exception.message == "the result of remote's request is null"
                        if (deleted) {
                            println("取消收藏(id=$id)")
                            getArticles()
                            message.value = "已取消收藏"
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    fun deleteWebsite(id: Int) {
        async {
            repo.deleteWebsite(id).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                    }
                    is HttpResult.Error -> {
                        if (response.exception.message == "the result of remote's request is null") {
                            webUrlList.value?.remove(webUrlList.value?.find { it.id == id })
                            message.value = "删除成功"
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("CollectionViewModel ==> onClear")
    }


}