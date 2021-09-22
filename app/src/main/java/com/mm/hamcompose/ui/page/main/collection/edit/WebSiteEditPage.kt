package com.mm.hamcompose.ui.page.main.collection.edit

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebSiteEditPage(
    website: ParentBean?,
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: WebSiteEditViewModel = hiltViewModel()
) {

    if (website != null) {
        viewModel.webSiteTitle.value = website.name
        viewModel.linkUrl.value = website.link
    }

    var snackLabel by remember { mutableStateOf(SNACK_ERROR) }
    var index by remember { mutableStateOf(0) }
    val titles by remember { viewModel.titles }
    var isSaved by remember { viewModel.isSaved }
    val webSiteTitle by remember { viewModel.webSiteTitle }
    val linkUrl by remember { viewModel.linkUrl }
    val author by remember { viewModel.author }
    var errorMessage by remember { viewModel.errorMessage }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineState = rememberCoroutineScope()

    if (isSaved) {
        navCtrl.back()
        viewModel.isSaved.value = false
    }

    if (errorMessage.isNotEmpty()) {
        popupSnackBar(coroutineState, scaffoldState, snackLabel, errorMessage)
        errorMessage = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        keyboardController?.hide()
                    }
                )
            }
    ) {

        HamToolBar(
            title = if (website == null) "添加收藏" else "编辑收藏",
            onBack = { navCtrl.back() },
            rightText = stringResource(id = R.string.save),
            onRightClick = {
                keyboardController?.hide()
                if (webSiteTitle.isNullOrEmpty()) {
                    snackLabel = SNACK_WARN
                    errorMessage = "标题不能为空"
                    return@HamToolBar
                }
                if (index == 1 && author.isNullOrEmpty()) {
                    snackLabel = SNACK_WARN
                    errorMessage = "作者不能为空"
                    return@HamToolBar
                }
                if (linkUrl.isNullOrEmpty()) {
                    snackLabel = SNACK_WARN
                    errorMessage = "链接不能为空"
                    return@HamToolBar
                }
                if (website != null) {
                    viewModel.saveNewCollect(-1, website.id)
                } else {
                    viewModel.saveNewCollect(index)
                }
            }
        )

        if (website == null) {
            SwitchTabBar(
                titles = titles,
                selectIndex = index,
                onSwitchClick = { index = it })
        }

        LazyColumn {
            item {
                LabelEditView(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    text = webSiteTitle ?: "",
                    labelText = stringResource(id = R.string.title),
                    hintText = stringResource(id = R.string.hint_text_title),
                    onValueChanged = {
                        viewModel.webSiteTitle.value = it
                    },
                    onDeleteClick = { viewModel.webSiteTitle.value = "" },
                )
            }
            if (index == 1) {
                item {
                    LabelEditView(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                        text = author ?: "",
                        labelText = stringResource(id = R.string.author),
                        hintText = stringResource(id = R.string.hint_text_name),
                        onValueChanged = {
                            viewModel.author.value = it
                        },
                        onDeleteClick = { viewModel.author.value = "" },
                    )
                }
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