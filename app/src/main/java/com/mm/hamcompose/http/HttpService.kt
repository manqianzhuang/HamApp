package com.mm.hamcompose.http

import com.mm.hamcompose.bean.*
import retrofit2.http.*

/**
 * 网络请求接口
 * 注意：接口前无需加斜杠
 * create by Mqz at 4/19
 */
interface HttpService {

    companion object {
        const val url = "https://www.wanandroid.com/"
    }

    @GET("article/list/{page}/json")
    suspend fun getIndexList(@Path("page") page: Int): ListWrapperBean<Article>

    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ListWrapperBean<Article>

    @GET("wenda/list/{page}/json")
    suspend fun getWendaData(@Path("page") page: Int): ListWrapperBean<Article>

    @GET("article/top/json")
    suspend fun getTopArticles(): ListBean<Article>

    @GET("hotkey/json")
    suspend fun getHotkeys(): ListBean<Hotkey>

    @GET("banner/json")
    suspend fun getBanners(): ListBean<BannerBean>

    @GET("tree/json")
    suspend fun getSystemList(): ListBean<ParentBean>

    @GET("navi/json")
    suspend fun getNavigationList(): ListBean<NaviWrapper>

    @GET("wxarticle/chapters/json")
    suspend fun getPublicInformation(): ListBean<ParentBean>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicArticles(
        @Path("id") publicId: Int,
        @Path("page") page: Int
    ): ListWrapperBean<Article>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicArticlesWithKey(
        @Path("id") publicId: Int,
        @Path("page") page: Int,
        @Query("k") key: String
    ): ListWrapperBean<Article>

    @GET("project/tree/json")
    suspend fun getProjectCategory(): ListBean<ParentBean>

    @GET("project/list/{page}/json")
    suspend fun getProjects(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ListWrapperBean<Article>

    @GET("article/list/{page}/json")
    suspend fun getSystemArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ListWrapperBean<Article>

    @GET("article/list/{page}/json")
    suspend fun getArticlesByAuthor(
        @Path("page") page: Int,
        @Query("author") author: String
    ): ListWrapperBean<Article>

    @GET("article/listproject/{page}/json")
    suspend fun getHotProjects(@Path("page") page: Int): ListWrapperBean<Article>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun queryArticle(
        @Path("page") page: Int,
        @Field("k") key: String
    ): ListWrapperBean<Article>

    // 福利
    @GET("https://gank.io/api/data/{type}/{pageCount}/{page}")
    suspend fun getWelfareList(
        @Path(value = "type", encoded = false) type: String,
        @Path("pageCount") pageCount: Int,
        @Path("page") page: Int
    ): WelfareBean


}
