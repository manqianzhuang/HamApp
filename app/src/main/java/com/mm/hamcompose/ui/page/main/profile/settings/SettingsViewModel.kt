package com.mm.hamcompose.ui.page.main.profile.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.THEME_COLOR_KEY
import com.mm.hamcompose.theme.themeColors
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: UserInfoDatabase
): BaseViewModel<UserInfo>() {

    val cacheSize = mutableStateOf(0)

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
                val deleteResult = db.userInfoDao().deleteAllUserInfo()
                LogUtils.i("deleteResult = $deleteResult")
            }
        }
    }

}