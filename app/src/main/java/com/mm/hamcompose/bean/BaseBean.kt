package com.mm.hamcompose.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mm.hamcompose.theme.HamTheme
import kotlinx.parcelize.Parcelize
import java.util.*

data class HomeThemeBean(
    var theme: HamTheme.Theme
)

//HTTP
data class ListBean<T>(
    var data: MutableList<T>,
    var errorCode: Int,
    var errorMsg: String
)

data class ListWrapperBean<T>(
    var data: ListWrapper<T>,
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


data class ParentBean(
    var children: MutableList<ParentBean>?,
    var courseId: Int,
    var id: Int,
    var name: String?,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readArrayList(ParentBean::class.java.classLoader) as MutableList<ParentBean>?,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(order)
        parcel.writeInt(parentChapterId)
        parcel.writeByte(if (userControlSetTop) 1 else 0)
        parcel.writeInt(visible)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParentBean> {
        override fun createFromParcel(parcel: Parcel): ParentBean {
            return ParentBean(parcel)
        }

        override fun newArray(size: Int): Array<ParentBean?> {
            return arrayOfNulls(size)
        }
    }

}

data class NaviWrapper(
    var articles: MutableList<Article>?,
    var cid: Int,
    var name: String?
)

data class Article(
    var apkLink: String?,
    var audit: Int,
    var author: String?,
    var canEdit: Boolean,
    var chapterId: Int,
    var chapterName: String?,
    var collect: Boolean,
    var courseId: Int,
    var desc: String?,
    var descMd: String?,
    var envelopePic: String?,
    var fresh: Boolean,
    var host: String?,
    var id: Int,
    var link: String?,
    var niceDate: String?,
    var niceShareDate: String?,
    var origin: String?,
    var prefix: String?,
    var projectLink: String?,
    var publishTime: Long,
    var realSuperChapterId: Int,
    var selfVisible: Int,
    var shareDate: Long,
    var shareUser: String?,
    var superChapterId: Int,
    var superChapterName: String?,
    var tags: MutableList<ArticleTag>?,
    var title: String?,
    var type: Int,
    var userId: Int,
    var visible: Int,
    var zan: Int
)

data class ArticleTag(
    var name: String,
    var url:String
)

@Entity(tableName = "hot_key")
data class Hotkey(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "link") var link: String?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "order") var order: Int,
    @ColumnInfo(name = "visible") var visible: Int
)

data class WebData(
    var title: String?,
    var url: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    companion object CREATOR : Parcelable.Creator<WebData> {
        override fun createFromParcel(parcel: Parcel): WebData {
            return WebData(parcel)
        }

        override fun newArray(size: Int): Array<WebData?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(url)
    }
}



/********************* Begin: Welfare Data ***********************/
abstract class GankBasedBean<T : Any?> {
    val error: Boolean = false
    abstract var results: T?
}

data class WelfareBean(override var results: List<WelfareData>?) : GankBasedBean<List<WelfareData>>()

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





