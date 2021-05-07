package com.mm.hamcompose.http.paging

import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.ListWrapperBean
import com.mm.hamcompose.http.HttpService

class SubscriptionPagingSource(
    private val api: HttpService,
    private val publicId: Int
): BasePagingSource<Article>() {

    override suspend fun call(page: Int): ListWrapperBean<Article> {
        return api.getPublicArticles(publicId, page)
    }


}