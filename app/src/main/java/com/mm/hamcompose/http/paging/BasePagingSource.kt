package com.mm.hamcompose.http.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.ListWrapperBean

abstract class BasePagingSource<T: Any>: PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
//    {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }

    abstract suspend fun call(page: Int): ListWrapperBean<T>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            LogUtils.e("当前页 ${params.key}")
            val page = params.key?: 0
            val response = call(page)
            val isNextPage = response.data.datas.size > 0
            LoadResult.Page(
                data = response.data.datas,
                prevKey = if (page>0) page-1 else null,
                nextKey = if (isNextPage) page+1 else null
            )
        } catch (e: Exception) {
            LogUtils.e("网络请求异常： ${e.message}")
            LoadResult.Error(e)
        }
    }


}