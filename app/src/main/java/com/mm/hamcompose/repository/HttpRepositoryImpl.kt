package com.mm.hamcompose.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WelfareBean
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.data.http.HttpService
import com.mm.hamcompose.data.http.paging.GirlPhotoPagingSource
import com.mm.hamcompose.data.http.paging.PagingFactory
import com.mm.hamcompose.ui.page.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HttpRepositoryImpl constructor(private val apiService: HttpService): BaseRepository(), HttpRepository {

    //banner
    override suspend fun getBanners(): BANNER = flowable { apiService.getBanners() }
    //置顶文章
    override suspend fun getTopArticles(): ARTICLE = flowable { apiService.getTopArticles() }
    //热门标签
    override suspend fun getHotkeys(): HOTKEY = flowable { apiService.getHotkeys() }
    //体系分类列表
    override suspend fun getStructureList(): PARENT = flowable { apiService.getStructureList() }
    //导航分类列表
    override suspend fun getNavigationList(): NAVIGATION = flowable { apiService.getNavigationList() }
    //公众号作者列表
    override suspend fun getPublicInformation(): PARENT = flowable { apiService.getPublicInformation() }
    //项目分类
    override suspend fun getProjectCategory(): PARENT = flowable { apiService.getProjectCategory() }
    //注册
    override suspend fun register(userName: String, password: String, repassword: String): USERINFO {
        return flowable { apiService.register(userName, password, repassword) }
    }
    //登录
    override suspend fun login(userName: String, password: String): USERINFO {
        return flowable { apiService.login(userName, password) }
    }
    //退出登录
    override suspend fun logout() = flowable { apiService.logout() }
    //我的积分排行
    override suspend fun getMyPointsRanking() = flowable { apiService.getMyPointsRanking() }
    override suspend fun getMessageCount() = flowable { apiService.getMessageCount() }
    override suspend fun getCollectUrls() = flowable { apiService.getCollectUrls() }
    override suspend fun collectInnerArticle(id: Int): ANY {
        return flowable { apiService.collectInnerArticle(id) }
    }
    override suspend fun uncollectInnerArticle(id: Int): ANY {
        return flowable { apiService.uncollectInnerArticle(id) }
    }
    override suspend fun uncollectArticleById(id: Int, originId: Int): ANY {
        return flowable { apiService.uncollectArticleById(id, originId) }
    }
    override suspend fun addNewWebsiteCollect(title: String, linkUrl: String): ONE_PARENT {
        return flowable { apiService.addNewWebsiteCollect(title, linkUrl) }
    }

    override suspend fun addNewArticleCollect(title: String, linkUrl: String, author: String): COLLECT {
        return flowable { apiService.addNewArticleCollect(title, linkUrl, author) }
    }

    override suspend fun deleteWebsite(id: Int) = flowable { apiService.deleteWebsite(id) }

    override suspend fun editCollectWebsite(id: Int, title: String, linkUrl: String): ANY {
        return flowable { apiService.editCollectWebsite(id, title, linkUrl) }
    }

    override suspend fun getMyShareArticles(page: Int): SHARER {
        return flowable { apiService.getMyShareArticles(page) }
    }

    override suspend fun getAuthorShareArticles(userId: Int, page: Int): SHARER {
        return flowable { apiService.getAuthorShareArticles(userId, page) }
    }

    override suspend fun deleteMyShareArticle(articleId: Int): ANY {
        return flowable { apiService.deleteMyShareArticle(articleId) }
    }

    override suspend fun addMyShareArticle(title: String, link: String, shareUser: String): ANY {
        return flowable { apiService.addMyShareArticle(title, link, shareUser) }
    }

    override suspend fun getBasicUserInfo(): BASIC_USERINFO {
        return flowable { apiService.getBasicUserInfo() }
    }

    //福利
    override suspend fun getWelfareData(page: Int): Flow<HttpResult<WelfareBean>> {
        return flow {
            val result =  try {
                val response = apiService.getWelfareList("福利", 20, page)
                if (response.results!=null) {
                    HttpResult.Success(response)
                } else {
                    throw Exception("the result of remote's request is null")
                }
            } catch (ex: Exception) {
                HttpResult.Error(ex)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /** 首页列表 */
    override fun getIndexData() = pager { page->  apiService.getIndexList(page) }

    /** 广场列表 */
    override fun getSquareData() = pager { page->  apiService.getSquareData(page) }

    /** 问答列表 */
    override fun getWendaData() = pager { page-> apiService.getWendaData(page) }

    /** 分类项目 （根据cid区分项目）*/
    override fun getProjects(cId: Int) = pager { page ->
        // -1=无分类，加载热门项目
        if (cId == -1) {
            apiService.getHotProjects(page)
        } else {
            apiService.getProjects(page, cId)
        }
    }

    /** 公众号文章 */
    override fun getPublicArticles(publicId: Int) = pager { apiService.getPublicArticles(publicId, it) }

    /** 体系 */
    override fun getStructureArticles(param: Any): PagingArticle {
        return pager { page ->
            when (param) {
                is String -> apiService.getArticlesByAuthor(page, param)
                else -> apiService.getStructureArticles(page, param as Int)
            }
        }
    }

    /** 搜索公众号*/
    override fun searchArticleWithKey(publicId: Int, key: String): PagingArticle {
        return pager{ page ->
            apiService.getPublicArticlesWithKey(publicId, page, key)
        }
    }

    /** 搜索文章*/
    override fun queryArticle(key: String) = pager { page -> apiService.queryArticle(page, key) }

    /** 积分排行榜 */
    override fun getPointsRankings(): PagingPoints {
        return pager(
            initKey = 1,
            baseConfig = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                prefetchDistance = 10
            )
        ) { page -> apiService.getPointsRankings(page) }
    }

    /** 积分记录 */
    override fun getPointsRecords() = pager(initKey = 1) { page -> apiService.getPointsRecords(page) }
    /** 收藏列表 */
    override fun getCollectionList() = pager { page -> apiService.getCollectionList(page) }
    /** 未读消息 */
    override fun getUnreadMessages() = pager(initKey = 1) { page -> apiService.getUnreadMessage(page) }
    /** 已读消息 */
    override fun getReadedMessages() = pager(initKey = 1) { page -> apiService.getUnreadMessage(page) }

    /** 看妹纸*/
    override fun getWelfareData(key: String): PagingWelfare {
        return Pager(
            config = PagingFactory().pagingConfig,
            pagingSourceFactory = {
                GirlPhotoPagingSource(apiService, key)
            }
        ).flow
    }


}