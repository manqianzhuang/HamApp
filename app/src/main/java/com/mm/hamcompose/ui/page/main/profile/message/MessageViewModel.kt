package com.mm.hamcompose.ui.page.main.profile.message

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingAny
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val repo: HttpRepository): BaseViewModel<Any>() {


    var tabIndex = mutableStateOf(0)
    var errorMessage = mutableStateOf<String?>(null)
    var pagingUnread = MutableLiveData<PagingAny?>(null)
    var pagingReaded = MutableLiveData<PagingAny?>(null)
    val titles = mutableStateOf(mutableListOf(
        TabTitle(401, "未读消息"),
        TabTitle(402, "已读消息"),
    ))


    override fun start() {
        initThat {
            pagingUnread.value = unread()
            pagingReaded.value = readed()
        }
    }

    fun refreshUnreadData() {
        pagingUnread.value = null
        pagingUnread.value = unread()
    }

    fun refreshReadedData() {
        pagingReaded.value = null
        pagingReaded.value = readed()
    }

    private fun unread() = repo.getUnreadMessages().cachedIn(viewModelScope)

    private fun readed() = repo.getReadedMessages().cachedIn(viewModelScope)
}