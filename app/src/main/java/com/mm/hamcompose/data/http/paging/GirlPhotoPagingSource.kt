package com.mm.hamcompose.data.http.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.WelfareData
import com.mm.hamcompose.data.http.HttpService
import javax.inject.Inject

class GirlPhotoPagingSource @Inject constructor(
    private val apiService: HttpService,
): PagingSource<Int, WelfareData>() {

    override fun getRefreshKey(state: PagingState<Int, WelfareData>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WelfareData> {
        return try {
            LogUtils.e("currentPage= ${params.key}, size=${params.loadSize}")
            val page = params.key?: 0
            val response = apiService.getWelfareList("Girl", "Girl", page, params.loadSize)
            val isNextPage = response.data!!.isNotEmpty()
            LoadResult.Page(
                data = response.data!!,
                prevKey = if (page>0) page-1 else null,
                nextKey = if (isNextPage) page+1 else null
            )
        } catch (e: Exception) {
            LogUtils.e("网络请求异常： ${e.message}")
            LoadResult.Error(e)
        }
    }
}