package com.mm.hamcompose.ui.page.base

interface IViewContract {

    fun loadContent()
    fun stopLoading()

    /**
     * 首页
     */
    interface IHomeView {
        fun loadBanners()       //获取banner数据
        fun loadTopArticles()   //获取置顶数据
    }

    /**
     * 有二级分类的情况下
     */
    interface ISubMenuView {
       fun loadCategory()   //获取分类
    }

    /**
     * 搜索模块
     */
    interface ISearch {
        fun search(key: String) //搜索
        fun getHistory()        //历史记录
        fun getHotkey()         //热门搜索
    }

    interface ISystem {
        fun searchArticleByAuthor(author: String)   //搜索某作者的文章
        fun searchArticleById(cid: Int)             //搜索某分类ID的文章
    }

}