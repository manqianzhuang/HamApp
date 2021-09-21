package com.mm.hamcompose.ui.page.main.profile

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: UserInfoDatabase
) : BaseViewModel<Article>() {

    val isLogin = mutableStateOf(false)
    val messageCount = mutableStateOf(0)
    var userInfo = mutableStateOf<UserInfo?>(null)
    var page = mutableStateOf(1)
    var myPoints = mutableStateOf<PointsBean?>(null)
    val myArticles = mutableStateOf(mutableListOf<Article>())

    override fun start() {
        initThat { checkLoginState() }
    }

    private fun initUserRemoteData() {
        initMessageCount()
        initBasicUserInfo()
        getMyShareArticles()
    }

    private fun initBasicUserInfo() {
        async {
            repo.getBasicUserInfo().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        myPoints.value = response.result.coinInfo
                        userInfo.value = response.result.userInfo
                        insertNewestUserInfo(response.result.userInfo)
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                    }
                }
            }
        }
    }

    private suspend fun insertNewestUserInfo(user: UserInfo) {
        withContext(Dispatchers.IO) {
            db.userInfoDao().insertUserInfo(user)
        }
    }

    private fun initMessageCount() {
        async {
            repo.getMessageCount().collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        messageCount.value = response.result
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                    }
                }
            }
        }
    }


    private fun checkLoginState() {
        async {
            flow { emit(db.userInfoDao().queryUserInfo()) }
                .flowOn(Dispatchers.IO)
                .collectLatest { users ->
                    isLogin.value = users.isNotEmpty()
                    if (users.isNotEmpty()) {
                        userInfo.value = users[0]
                        initUserRemoteData()
                    }
                }
        }
    }

    private fun getMyShareArticles() {
        async {
            repo.getMyShareArticles(page.value).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        myPoints.value = response.result.coinInfo
                        myArticles.value = response.result.shareArticles.datas
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                    }
                }
            }
        }
    }

}