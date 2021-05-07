package com.mm.hamcompose.http.paging

import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.ListWrapperBean
import com.mm.hamcompose.http.HttpService
import javax.inject.Inject

class IndexPagingSource @Inject constructor(private val apiService: HttpService) : BasePagingSource<Article>() {
    override suspend fun call(page: Int): ListWrapperBean<Article> = apiService.getIndexList(page)
}