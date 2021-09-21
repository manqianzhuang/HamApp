package com.mm.hamcompose.data.bean

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mm.hamcompose.data.db.DbConst
import com.mm.hamcompose.theme.HamTheme
import kotlinx.parcelize.Parcelize
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

data class HomeThemeBean(
    var theme: HamTheme.Theme
)

data class BasicBean<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)

data class ListWrapper<T>(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: ArrayList<T>
)

data class BannerBean(
    var desc: String?,
    var id: Int,
    var imagePath: String?,
    var isVisible: Int,
    var order: Int,
    var title: String?,
    var type: Int,
    var url: String?
)

data class SharerBean<T>(
    val coinInfo: PointsBean,
    val shareArticles: ListWrapper<T>
)

data class BasicUserInfo(
    val coinInfo: PointsBean,
    val userInfo: UserInfo
)

@Parcelize
data class ParentBean(
    var children: MutableList<ParentBean>?,
    var courseId: Int = -1,
    var id: Int = -1,
    var name: String? = "分类",
    var order: Int = -1,
    var parentChapterId: Int = -1,
    var userControlSetTop: Boolean = false,
    var visible: Int = -1,
    var icon: String? = null,
    var link: String? = null
): Parcelable

data class NaviWrapper(
    var articles: MutableList<Article>?,
    var cid: Int,
    var name: String?
)

@Parcelize
data class Article(
    var apkLink: String? = "",
    var audit: Int = -1,
    var author: String? = "作者",
    var canEdit: Boolean = false,
    var chapterId: Int = -1,
    var chapterName: String? = "章节",
    var collect: Boolean = false,
    var courseId: Int = -1,
    var desc: String? = "描述",
    var descMd: String? = "描述Md",
    var envelopePic: String? = "图片1",
    var fresh: Boolean = false,
    var host: String? = "https://www.wanandroid.com",
    var id: Int = -1,
    var link: String? = "https://www.wanandroid.com",
    var niceDate: String? = "1970-0-0",
    var niceShareDate: String? = "1970-0-0",
    var origin: String? = "",
    var prefix: String? = "",
    var projectLink: String? = "https://www.wanandroid.com",
    var publishTime: Long = 0L,
    var realSuperChapterId: Int = -1,
    var selfVisible: Int = -1,
    var shareDate: Long = 0L,
    var shareUser: String? = "分享者",
    var superChapterId: Int = -1,
    var superChapterName: String? = "超级分类",
    var tags: MutableList<ArticleTag>? = null,
    var title: String? = "标题",
    var type: Int = -1,
    var userId: Int = -1,
    var visible: Int = -1,
    var zan: Int = -1
): Parcelable

@Parcelize
data class ArticleTag(
    var name: String,
    var url:String
): Parcelable

@Entity(tableName = DbConst.hotKey)
data class Hotkey(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var link: String?,
    var name: String?,
    var order: Int,
    var visible: Int
)

@Parcelize
data class WebData(
    var title: String?,
    var url: String
): Parcelable

@Entity(tableName = DbConst.userInfo)
@TypeConverters(IntTypeConverter::class)
@Parcelize
data class UserInfo(
    @PrimaryKey var id: Int,
    var admin: Boolean,
    var chapterTops: MutableList<Int>,
    var coinCount: Int,
    var collectIds: MutableList<Int>,
    var email: String,
    var icon: String,
    var nickname: String,
    var password: String,
    var token: String,
    var type: Int,
    var username: String,
): Parcelable

object IntTypeConverter {

    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun fromJson(value: String): List<Int> {
        return Gson().fromJson(value, typeOf<MutableList<Int>>().javaType)
    }

    @TypeConverter
    fun toJson(json: MutableList<Int>): String {
        return Gson().toJson(json)
    }
}

data class PointsBean(
    var id: Int?,
    var coinCount: String,
    var level: Int?,
    var nickname: String,
    var rank: String?,
    var userId: Int,
    var username: String,
    var date: String?,
    var desc: String?,
    var reason: String?,
    var type: Int?,
)

data class CollectBean(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)


/********************* Begin: Welfare Data ***********************/
abstract class GankBasedBean<T : Any?> {
    val error: Boolean = false
    abstract var results: T?
}

data class WelfareBean(
    override var results: List<WelfareData>?
) : GankBasedBean<List<WelfareData>>()

@Parcelize
data class WelfareData(
    val _id: String,
    val createAt: String?,
    val desc: String?,
    val publishedAt: String?,
    val source: String?,
    val type: String?,
    val url: String,
    val used: Boolean,
    val who: String?
) : Parcelable
/********************* Begin: Welfare Data ***********************/





