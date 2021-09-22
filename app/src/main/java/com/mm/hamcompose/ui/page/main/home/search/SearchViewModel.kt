package com.mm.hamcompose.ui.page.main.home.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.Hotkey
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.data.db.hotkey.HotkeyDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingArticle
import com.mm.hamcompose.ui.page.base.BaseCollectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val hotkeyDatabase: HotkeyDatabase,
    private val historyDatabase: HistoryDatabase,
): BaseCollectViewModel<Article>(repo) {

    //搜索列表
    val searches = MutableLiveData<PagingArticle?>(null)
    //搜索词的历史记录
    val history = mutableStateOf(mutableListOf<Hotkey>())
    //搜索的热词
    val hotkeys = mutableStateOf(mutableListOf<Hotkey>())
    val searchContent = mutableStateOf("")

    override fun start() {
        initThat {
            getHotkey()
            getHistory()
        }
    }

    fun search(key: String) {
        searches.value = repo.queryArticle(key).cachedIn(viewModelScope)
    }

    //插入数据
    fun insertKey(key: String) {
        if (hasTheSame(key))
            return
        async {
            val bean = Hotkey(link = "", name = key, order = 1, visible = 0)
            hotkeyDatabase.hotkeyDao().insertHotkeys(bean)
            update()
        }
    }

    //判断是否已经存在搜索词
    private fun hasTheSame(key: String): Boolean {
        val same = history.value.indexOfFirst { it.name.equals(key, ignoreCase = true) } != -1
        LogUtils.e("是否相同搜索词 = $same")
        return same
    }

    //删除单条数据
    fun deleteKey(key: Hotkey) {
        async {
            withContext(Dispatchers.IO) {
                hotkeyDatabase.hotkeyDao().deleteKeys(key)
            }
            update()
        }
    }

    //删除所有数据
    fun deleteAll() {
        async {
            hotkeyDatabase.hotkeyDao().deleteAll()
            update()
        }
    }

    private fun update() = getHistory()

    //查询所有数据
    fun getHistory() {
        async {
            val result = hotkeyDatabase.hotkeyDao().loadAllKeys()
            withContext(Dispatchers.Main) {
                history.value = result.toMutableList()
            }
        }
    }

    fun getHotkey() {
        async {
            repo.getHotkeys().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        hotkeys.value = response.result
                    }
                    is HttpResult.Error -> {

                    }
                }
            }
        }
    }

    fun saveDataToHistory(article: Article) {
        cacheHistory(historyDatabase, article)
    }


}