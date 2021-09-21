package com.mm.hamcompose.ui.page.main.profile.user

import androidx.compose.runtime.mutableStateOf
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val userDb: UserInfoDatabase
) : BaseViewModel<UserInfo>() {

    var errorMessage = mutableStateOf<String?>(null)
    var isRegister = mutableStateOf(false)
    var isLogin = mutableStateOf(false)

    override fun start() {}

    fun register(account: String, password: String, repassword: String) {
        async {
            repo.register(account, password, repassword)
                .collectLatest { response ->
                    when (response) {
                        is HttpResult.Success -> {
                            isRegister.value = true
                        }
                        is HttpResult.Error -> {
                            errorMessage.value = response.exception.message
                            //ToastUtils.showShort(errorMessage.value)
                        }
                    }
                }
        }
    }

    fun login(account: String, password: String) {
        async {
            repo.login(account, password)
                .collectLatest { response ->
                    when (response) {
                        is HttpResult.Success -> {
                            saveUserInfo(response.result)
                            isLogin.value = true
                        }
                        is HttpResult.Error -> {
                            errorMessage.value = response.exception.message
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        LogUtils.e("invoke onCleared of ViewModel")
    }

    private fun saveUserInfo(userInfo: UserInfo) {
        async {
            withContext(Dispatchers.IO) {
                userDb.userInfoDao().insertUserInfo(userInfo)
            }
        }
    }
}