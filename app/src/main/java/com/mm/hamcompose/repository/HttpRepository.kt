package com.mm.hamcompose.repository

import androidx.paging.PagingData
import com.mm.hamcompose.data.bean.*
import com.mm.hamcompose.data.http.HttpResult
import kotlinx.coroutines.flow.Flow

//类型别名，用于定义较长的泛型类型
typealias BANNER = Flow<HttpResult<MutableList<BannerBean>>>
typealias ARTICLE = Flow<HttpResult<MutableList<Article>>>
typealias HOTKEY = Flow<HttpResult<MutableList<Hotkey>>>
typealias PARENT = Flow<HttpResult<MutableList<ParentBean>>>
typealias NAVIGATION = Flow<HttpResult<MutableList<NaviWrapper>>>
typealias USERINFO = Flow<HttpResult<UserInfo>>
typealias POINTS = Flow<HttpResult<PointsBean>>
typealias ANY = Flow<HttpResult<Any>>
typealias COLLECT = Flow<HttpResult<CollectBean>>
typealias SHARER = Flow<HttpResult<SharerBean<Article>>>
typealias ONE_PARENT = Flow<HttpResult<ParentBean>>
typealias BASIC_USERINFO = Flow<HttpResult<BasicUserInfo>>

typealias WELFARE = Flow<HttpResult<WelfareBean>>

typealias PagingAny = Flow<PagingData<Any>>
typealias PagingPoints = Flow<PagingData<PointsBean>>
typealias PagingCollect = Flow<PagingData<CollectBean>>
typealias PagingArticle = Flow<PagingData<Article>>
typealias PagingWelfare = Flow<PagingData<WelfareData>>

interface HttpRepository {
    //普通请求
    suspend fun getBanners(): BANNER
    suspend fun getTopArticles(): ARTICLE
    suspend fun getHotkeys(): HOTKEY
    suspend fun getStructureList(): PARENT
    suspend fun getNavigationList(): NAVIGATION
    suspend fun getPublicInformation(): PARENT
    suspend fun getProjectCategory(): PARENT
    suspend fun register(userName: String, password: String, repassword: String): USERINFO
    suspend fun login(userName: String, password: String): USERINFO
    suspend fun logout(): ANY
    suspend fun getMyPointsRanking(): POINTS
    suspend fun getMessageCount(): Flow<HttpResult<Int>>
    suspend fun getCollectUrls(): PARENT
    suspend fun collectInnerArticle(id: Int): ANY
    suspend fun uncollectInnerArticle(id: Int): ANY
    suspend fun uncollectArticleById(id: Int, originId: Int): ANY
    suspend fun addNewWebsiteCollect(title: String, linkUrl: String): ONE_PARENT
    suspend fun addNewArticleCollect(title: String, linkUrl: String, author: String): COLLECT
    suspend fun deleteWebsite(id: Int): ANY
    suspend fun editCollectWebsite(id: Int, title: String, linkUrl: String): ANY
    suspend fun getMyShareArticles(page: Int): SHARER
    suspend fun getAuthorShareArticles(userId: Int, page: Int): SHARER
    suspend fun deleteMyShareArticle(articleId: Int): ANY
    suspend fun addMyShareArticle(title: String, link: String, shareUser: String): ANY
    suspend fun getBasicUserInfo(): BASIC_USERINFO

    //干货 gank.io的妹纸福利列表
    suspend fun getWelfareData(page: Int): WELFARE

    //分页请求
    fun getIndexData(): PagingArticle
    fun getSquareData(): PagingArticle
    fun getWendaData(): PagingArticle
    fun getProjects(cId: Int): PagingArticle
    fun getPublicArticles(publicId: Int): PagingArticle
    fun getStructureArticles(param: Any): PagingArticle
    fun searchArticleWithKey(publicId: Int, key: String): PagingArticle
    fun queryArticle(key: String): PagingArticle
    fun getPointsRankings(): PagingPoints
    fun getPointsRecords(): PagingPoints
    fun getCollectionList(): PagingCollect
    fun getUnreadMessages(): PagingAny
    fun getReadedMessages(): PagingAny
    fun getWelfareData(key: String): PagingWelfare

}