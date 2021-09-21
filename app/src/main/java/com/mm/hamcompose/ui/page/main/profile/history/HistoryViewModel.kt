package com.mm.hamcompose.ui.page.main.profile.history

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.HistoryRecord
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val db: HistoryDatabase): BaseViewModel<HistoryRecord>() {

    var isClear = mutableStateOf(false)

    override fun start() {
        initThat { getHistoryList() }
    }

    private fun getHistoryList() {
        async {
           val history = withContext(Dispatchers.IO) { db.historyDao().queryAll() }
            withContext(Dispatchers.Main) {
                list.value = history.toMutableList()
            }
        }
    }

    fun clearAllHistory() {
        async {
            withContext(Dispatchers.IO) { db.historyDao().deleteAll() }
            withContext(Dispatchers.Main) {
                list.value = mutableListOf()
                isClear.value = true
            }
        }
    }

}