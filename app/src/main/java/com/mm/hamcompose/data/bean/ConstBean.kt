package com.mm.hamcompose.data.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mm.hamcompose.data.db.DbConst

const val MY_USER_ID = -999

data class MenuTitle(
    val title: String,
    val iconRes: Int?
)

data class TabTitle(
    val id: Int,
    val text: String,
    var cachePosition: Int = 0,
    var selected: Boolean = false
)

@Entity(tableName = DbConst.history)
data class HistoryRecord(
    @PrimaryKey var id: Int,
    var title: String,
    var link: String,
    var niceDate: String,
    var shareUser: String,
    var userId: Int,
    var author: String,
    var superChapterId: Int,
    var superChapterName: String,
    var chapterId: Int,
    var chapterName: String,
    var desc: String,
)