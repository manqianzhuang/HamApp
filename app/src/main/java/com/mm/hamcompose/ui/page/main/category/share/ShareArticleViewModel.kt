package com.mm.hamcompose.ui.page.main.category.share

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ShareArticleViewModel @Inject constructor(
    private val repo: HttpRepository,
    private val db: UserInfoDatabase
): BaseViewModel<Article>() {

    var title = mutableStateOf<String?>(null)
    var shareUser = mutableStateOf<String?>(null)
    var linkUrl = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf("")

    override fun start() {

    }

    fun addShareArticle() {
        async {
            if (shareUser.value.isNullOrEmpty()) {
                val users = db.userInfoDao().queryUserInfo()
                if (!users.isNullOrEmpty()) {
                    with(users[0]!!) {
                        shareUser.value = if (nickname.isEmpty()) username else nickname
                    }
                }
            }
            repo.addMyShareArticle(title.value!!, linkUrl.value!!, shareUser.value!!)
                .collectLatest { response ->
                    when(response) {
                        is HttpResult.Success -> { }
                        is HttpResult.Error -> {
                            val isShare = response.exception.message == "the result of remote's request is null"
                            if (isShare) {
                                errorMessage.value = "分享成功"
                            }
                        }
                    }
                }

        }
    }

}