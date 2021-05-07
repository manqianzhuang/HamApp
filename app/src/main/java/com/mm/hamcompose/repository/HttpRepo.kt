package com.mm.hamcompose.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.*
import com.mm.hamcompose.http.HttpService
import com.mm.hamcompose.http.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HttpRepo @Inject constructor(private val apiService: HttpService){

    suspend fun getBanners(): Flow<ListBean<BannerBean>> = flowable(apiService.getBanners())

    suspend fun getTopArticles(): Flow<ListBean<Article>> = flowable(apiService.getTopArticles())

    suspend fun getHotkeys(): Flow<ListBean<Hotkey>> = flowable(apiService.getHotkeys())

    suspend fun getSystemList(): Flow<ListBean<ParentBean>> = flowable(apiService.getSystemList())

    suspend fun getNavigationList(): Flow<ListBean<NaviWrapper>> = flowable(apiService.getNavigationList())

    suspend fun getPublicInformation(): Flow<ListBean<ParentBean>> = flowable(apiService.getPublicInformation())

    suspend fun getProjectCategory(): Flow<ListBean<ParentBean>> = flowable(apiService.getProjectCategory())

    suspend fun getWelfareData(page: Int): Flow<WelfareBean> =
        flowable(apiService.getWelfareList("福利", 20, page))

    private fun <T> flowable(data: T): Flow<T> = flow { emit(data) }.flowOn(Dispatchers.IO)

    fun getIndexData(): Flow<PagingData<Article>> {
        return Pager(PagingFactory().pagingConfig) {
            IndexPagingSource(apiService)
        }.flow
    }

    fun getSquareData(): Flow<PagingData<Article>> {
        return Pager(PagingFactory().pagingConfig) {
            SquarePagingSource(apiService)
        }.flow
    }

    fun getWendaData(): Flow<PagingData<Article>> {
        return Pager(PagingFactory().pagingConfig) {
            WendaPagingSource(apiService)
        }.flow
    }

    fun getHotProjects(): Flow<PagingData<Article>> {
        return Pager(PagingFactory().pagingConfig) {
            HotProjectPagingSource(apiService)
        }.flow
    }

    fun getProjectData(cId: Int): Flow<PagingData<Article>> {
        LogUtils.e("加载项目列表 cid = $cId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            ProjectPagingSource(apiService, cId)
        }.flow
    }

    fun getPublicArticles(publicId: Int): Flow<PagingData<Article>> {
        LogUtils.e("加载公众号文章列表 publicId = $publicId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SubscriptionPagingSource(apiService, publicId)
        }.flow
    }

    fun getSystemArticles(param: Any): Flow<PagingData<Article>> {
        LogUtils.e("加载体系文章 cid = $param")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SystemArticlePagingSource(apiService, param)
        }.flow
    }

    fun searchArticleWithKey(publicId: Int, key: String): Flow<PagingData<Article>> {
        LogUtils.e("搜索$key 公众号文章 publicId = $publicId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SubscriptionSearchPagingSource(apiService, publicId, key)
        }.flow
    }

    fun queryArticle(key: String): Flow<PagingData<Article>> {
        LogUtils.e("query $key")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SearchPagingSource(apiService, key)
        }.flow
    }


    fun getWelfareData(key: String): Flow<PagingData<WelfareData>> {
        LogUtils.e("query $key")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            GirlPhotoPagingSource(apiService, key)
        }.flow
    }


}