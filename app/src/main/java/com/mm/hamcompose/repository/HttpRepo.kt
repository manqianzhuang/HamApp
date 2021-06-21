package com.mm.hamcompose.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.*
import com.mm.hamcompose.http.HttpService
import com.mm.hamcompose.http.paging.*
import com.mm.hamcompose.ui.page.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//类型别名，用于缩短较长的泛型类型
typealias BANNER = Flow<ListBean<BannerBean>>
typealias ARTICLE = Flow<ListBean<Article>>
typealias HOTKEY = Flow<ListBean<Hotkey>>
typealias PARENT = Flow<ListBean<ParentBean>>
typealias NAVIGATION = Flow<ListBean<NaviWrapper>>

typealias PagArticle = Flow<PagingData<Article>>
typealias PagWelfare = Flow<PagingData<WelfareData>>

class HttpRepo @Inject constructor(private val apiService: HttpService): BaseRepository() {

    //banner
    suspend fun getBanners(): BANNER = flowable(apiService.getBanners())
    //置顶文章
    suspend fun getTopArticles(): ARTICLE = flowable(apiService.getTopArticles())
    //热门标签
    suspend fun getHotkeys(): HOTKEY = flowable(apiService.getHotkeys())
    //体系分类列表
    suspend fun getSystemList(): PARENT = flowable(apiService.getSystemList())
    //导航分类列表
    suspend fun getNavigationList(): NAVIGATION = flowable(apiService.getNavigationList())
    //公众号作者列表
    suspend fun getPublicInformation(): PARENT = flowable(apiService.getPublicInformation())
    //项目分类
    suspend fun getProjectCategory(): PARENT = flowable(apiService.getProjectCategory())
    //福利
    suspend fun getWelfareData(page: Int): Flow<WelfareBean> =
        flowable(apiService.getWelfareList("福利", 20, page))

    /**
     * 首页列表
     */
    fun getIndexData(): PagArticle {
        return Pager(PagingFactory().pagingConfig) {
            IndexPagingSource(apiService)
        }.flow
    }

    /**
     * 广场列表
     */
    fun getSquareData(): PagArticle {
        return Pager(PagingFactory().pagingConfig) {
            SquarePagingSource(apiService)
        }.flow
    }

    /**
     * 问答列表
     */
    fun getWendaData(): PagArticle {
        return Pager(PagingFactory().pagingConfig) {
            WendaPagingSource(apiService)
        }.flow
    }

    /**
     * 热门项目
     */
    fun getHotProjects(): PagArticle {
        return Pager(PagingFactory().pagingConfig) {
            HotProjectPagingSource(apiService)
        }.flow
    }

    /**
     * 分类项目 （根据cid区分项目）
     */
    fun getProjectData(cId: Int): PagArticle {
        LogUtils.e("加载项目列表 cid = $cId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            ProjectPagingSource(apiService, cId)
        }.flow
    }

    /**
     * 公众号文章
     */
    fun getPublicArticles(publicId: Int): PagArticle {
        LogUtils.e("加载公众号文章列表 publicId = $publicId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SubscriptionPagingSource(apiService, publicId)
        }.flow
    }

    /**
     * 体系
     */
    fun getSystemArticles(param: Any): PagArticle {
        LogUtils.e("加载体系文章 cid = $param")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SystemArticlePagingSource(apiService, param)
        }.flow
    }

    /**
     * 搜索公众号
     */
    fun searchArticleWithKey(publicId: Int, key: String): PagArticle {
        LogUtils.e("搜索$key 公众号文章 publicId = $publicId")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SubscriptionSearchPagingSource(apiService, publicId, key)
        }.flow
    }

    /**
     * 搜索文章
     */
    fun queryArticle(key: String): PagArticle {
        LogUtils.e("query $key")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            SearchPagingSource(apiService, key)
        }.flow
    }


    /**
     * 看妹纸
     */
    fun getWelfareData(key: String): PagWelfare {
        LogUtils.e("query $key")
        return Pager(
            config = PagingFactory().pagingConfig,
        ) {
            GirlPhotoPagingSource(apiService, key)
        }.flow
    }


}