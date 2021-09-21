package com.mm.hamcompose.data.http.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.BasicBean
import com.mm.hamcompose.data.bean.ListWrapper
import com.mm.hamcompose.data.http.HttpResult

class BasePagingSource<T: Any> constructor(
    private val callDataFromRemoteServer: suspend (page: Int)-> HttpResult<BasicBean<ListWrapper<T>>>
): PagingSource<Int, T>() {

    private var page: Int = -1

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        LogUtils.e("当前页 ${params.key}")
        page = params.key ?: 0
        return when (val response = callDataFromRemoteServer(page)) {
            is HttpResult.Success -> {
                val data = response.result.data
                val hasNotNext = (data!!.datas.size < params.loadSize) && (data.over)
                LoadResult.Page(
                    data = response.result.data!!.datas,
                    prevKey = if (page - 1 > 0) page - 1 else null,
                    nextKey = if (hasNotNext) null else page+1
                )
            }
            is HttpResult.Error -> {
                LogUtils.e("网络请求异常： ${response.exception.message}")
                LoadResult.Error(response.exception)
            }
        }
    }
}