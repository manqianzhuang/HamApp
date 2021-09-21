package com.mm.hamcompose.ui.page.base

import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.HistoryRecord
import com.mm.hamcompose.data.db.history.HistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class CacheHistoryViewModel<T>: BaseViewModel<T>() {

    fun cacheHistory(db: HistoryDatabase, article: Article) {
        async {
            val history = toMapData(article)
            withContext(Dispatchers.IO) {
                db.historyDao().insertHistory(history)
                println("成功储存到历史记录")
            }
        }
    }

    private fun toMapData(article: Article): HistoryRecord {
        return with(article) {
            HistoryRecord(
                id = id,
                title = title ?: "",
                link = link ?: "",
                niceDate = niceDate ?: "",
                shareUser = shareUser ?: "",
                userId = userId,
                author = author ?: "",
                superChapterId = superChapterId,
                superChapterName = superChapterName ?: "",
                chapterId = chapterId,
                chapterName = chapterName ?: "",
                desc = desc ?: ""
            )
        }
    }

}