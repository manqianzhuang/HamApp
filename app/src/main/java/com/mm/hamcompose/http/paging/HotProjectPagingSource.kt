package com.mm.hamcompose.http.paging

import com.mm.hamcompose.bean.ListWrapperBean
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.http.HttpService

class HotProjectPagingSource(
    private val api: HttpService
): BasePagingSource<Article>() {

    override suspend fun call(page: Int): ListWrapperBean<Article> {
        return api.getHotProjects(page)
    }


}