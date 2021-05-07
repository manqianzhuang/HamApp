package com.mm.hamcompose.http.paging

import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.ListWrapperBean
import com.mm.hamcompose.http.HttpService

class SystemArticlePagingSource(
    private val api: HttpService,
    private val param: Any
): BasePagingSource<Article>() {

    override suspend fun call(page: Int): ListWrapperBean<Article> {
        return when(param) {
            is String -> api.getArticlesByAuthor(page, param)
            else -> api.getSystemArticles(page, param as Int)
        }
    }


}