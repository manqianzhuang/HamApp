package com.mm.hamcompose.ui.page.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.Hotkey
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.repository.db.HotkeyDatabase
import com.mm.hamcompose.ui.page.base.BaseViewModel
import com.mm.hamcompose.ui.page.base.IViewContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

class SearchViewModel @ViewModelInject constructor(
    private val repo: HttpRepo,
    private val hotkeyDatabase: HotkeyDatabase
): BaseViewModel<Article>(), IViewContract.ISearch {

    //搜索列表
    val searches = MutableLiveData<Flow<PagingData<Article>>>()
    //搜索词的历史记录
    val history = MutableLiveData(mutableListOf<Hotkey>())
    //搜索的热词
    val hotkeys = MutableLiveData(mutableListOf<Hotkey>())

    override fun start() {
        if (hotkeys.value!!.isEmpty()) {
            getHotkey()
        }
        if (history.value!!.isEmpty()) {
            getHistory()
        }
    }

    override fun search(key: String) {
        searches.value = repo.queryArticle(key)
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
        val same = history.value?.indexOfFirst { it.name.equals(key, ignoreCase = true) } != -1
        LogUtils.e("是否相同搜索词 = $same")
        return same
    }

    //删除单条数据
    fun deleteKey(key: Hotkey) {
        async {
            hotkeyDatabase.hotkeyDao().deleteKeys(key)
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
    override fun getHistory() {
        async {
            val result = hotkeyDatabase.hotkeyDao().loadAllKeys()
            withContext(Dispatchers.Main) {
                history.value = result.toMutableList()
            }
        }
    }

    override fun getHotkey() {
        async {
            repo.getHotkeys()
                .collectLatest {
                    hotkeys.value = it.data
                }
        }
    }


}