package com.mm.hamcompose.ui.page.base

import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import kotlinx.coroutines.flow.collectLatest

abstract class BaseCollectViewModel<T> constructor(
    private val httpRepo: HttpRepository
) : CacheHistoryViewModel<T>() {

    fun collectArticleById(id: Int) {
        async {
            httpRepo.collectInnerArticle(id).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> { }
                    is HttpResult.Error -> {
                        //收藏接口，不走success判断分支
                        val nullNotice = "the result of remote's request is null"
                        if (response.exception.message==nullNotice) {
                            println("收藏成功(id=$id)")
                            message.value = "收藏成功"
                        }
                    }
                }
            }
        }
    }

    fun uncollectArticleById(id: Int) {
        async {
            httpRepo.uncollectInnerArticle(id).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> { }
                    is HttpResult.Error -> {
                        //收藏接口，不走success判断分支
                        val nullNotice = "the result of remote's request is null"
                        if (response.exception.message==nullNotice) {
                            println("取消收藏(id=$id)")
                            message.value = "取消收藏"
                        }
                    }
                }
            }
        }
    }
}