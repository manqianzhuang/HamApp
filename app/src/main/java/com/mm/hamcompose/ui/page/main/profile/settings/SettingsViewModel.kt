package com.mm.hamcompose.ui.page.main.profile.settings

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.blankj.utilcode.util.SPUtils
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.data.store.DataStoreUtils
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.theme.THEME_COLOR_KEY
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: UserInfoDatabase
): BaseViewModel<UserInfo>() {

    val cacheSize = mutableStateOf(0)

    var logout = mutableStateOf(false)
    var themeIndex = mutableStateOf(SPUtils.getInstance().getInt(THEME_COLOR_KEY, 0))
    var selectTheme = mutableStateOf<Color?>(null)


    override fun start() {
        initThat {  }
    }

    fun logout() {
        async {
            repo.logout().collectLatest {
                when(it) {
                    is HttpResult.Success -> { }
                    is HttpResult.Error -> {
                        //退出登录的情况下，不走success判断分支
                        val nullNotice = "the result of remote's request is null"
                        if (it.exception.message==nullNotice) {
                            clearUserInfo()
                        }
                    }
                }
            }
        }
    }

    private fun clearUserInfo() {
        async {
            withContext(Dispatchers.IO) {
                db.userInfoDao().deleteAllUserInfo()
                DataStoreUtils.clear()
                delay(10)
                logout.value = true
            }
        }
    }

}