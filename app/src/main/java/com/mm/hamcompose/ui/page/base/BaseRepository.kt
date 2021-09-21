package com.mm.hamcompose.ui.page.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mm.hamcompose.data.bean.BasicBean
import com.mm.hamcompose.data.bean.ListWrapper
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.data.http.paging.BasePagingSource
import com.mm.hamcompose.data.http.paging.PagingFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {

    fun <T> flowable(call: suspend ()-> BasicBean<T>): Flow<HttpResult<T>> {
        return flow {
            val result =  try {
                val response = call()
                if (response.errorCode==0) {
                    if (response.data!=null) {
                        HttpResult.Success(response.data!!)
                    } else {
                        throw Exception("the result of remote's request is null")
                    }
                } else {
                    throw Exception(response.errorMsg)
                }
            } catch (ex: Exception) {
                HttpResult.Error(ex)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun <T: Any> pager(
        initKey: Int = 0,
        baseConfig: PagingConfig = PagingFactory().pagingConfig,
        callAction: suspend (page: Int)-> BasicBean<ListWrapper<T>>
    ): Flow<PagingData<T>> {

        // config = 加载分页数据的配置项
        // initialKey = 设置默认的初始页
        // pagingSourceFactory = 加载分页的驱动器
        return Pager(
            config = baseConfig,
            initialKey = initKey,
            pagingSourceFactory = {
                BasePagingSource {
                    try {
                        HttpResult.Success(callAction(it))
                    } catch (e: Exception) {
                        HttpResult.Error(e)
                    }
                }
            }).flow
    }

}