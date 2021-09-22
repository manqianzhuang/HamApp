package com.mm.hamcompose.ui.page.main.category.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.R
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareArticlePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: ShareArticleViewModel = hiltViewModel()
) {

    val title by remember { viewModel.title }
    val shareUser by remember { viewModel.shareUser }
    val linkUrl by remember { viewModel.linkUrl }
    var errorMsg by remember { viewModel.errorMessage }
    var snackLabel by remember { mutableStateOf(SNACK_WARN) }
    val keyboardController = LocalSoftwareKeyboardController.current


    if (errorMsg.isNotEmpty()) {
        popupSnackBar(
            scope = rememberCoroutineScope(),
            scaffoldState = scaffoldState,
            label = if (errorMsg == "分享成功") SNACK_SUCCESS else snackLabel,
            message = errorMsg
        )
        errorMsg = ""
    }

    Column {
        HamToolBar(
            title = "分享文章",
            onBack = { navCtrl.back() },
            rightText = "保存",
            onRightClick = {
                keyboardController?.hide()
                if (title.isNullOrEmpty()) {
                    snackLabel = SNACK_WARN
                    errorMsg = "标题不能为空"
                    return@HamToolBar
                }
                if (linkUrl.isNullOrEmpty()) {
                    snackLabel = SNACK_WARN
                    errorMsg = "链接不能为空"
                    return@HamToolBar
                }
                viewModel.addShareArticle()
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                LabelEditView(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    text = title ?: "",
                    labelText = stringResource(id = R.string.title),
                    hintText = "请输入标题(限100字以内)",
                    onValueChanged = {
                        viewModel.title.value = it
                    },
                    onDeleteClick = { viewModel.title.value = "" },
                )
            }
            item {
                LabelEditView(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    text = shareUser ?: "",
                    labelText = stringResource(id = R.string.author),
                    hintText = "默认使用昵称，没有昵称则使用用户名",
                    onValueChanged = {
                        viewModel.shareUser.value = it
                    },
                    onDeleteClick = { viewModel.shareUser.value = "" },
                )
            }
            item {
                LabelEditView(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    text = linkUrl ?: "",
                    labelText = stringResource(id = R.string.link),
                    hintText = stringResource(id = R.string.hint_text_website_url),
                    onValueChanged = {
                        viewModel.linkUrl.value = it
                    },
                    onDeleteClick = { viewModel.linkUrl.value = "" },
                )
            }
        }
    }

}