package com.mm.hamcompose.ui.page.main.profile.user

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ToolBarHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: UserViewModel = hiltViewModel()
) {

    var userAccount by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    val isLogin by remember { viewModel.isLogin }
    val errorMessage by remember { viewModel.errorMessage }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineState = rememberCoroutineScope()

    /**
     * 重点：保存Bundle状态，以防从下一页返回时数据丢失
     */
    val accountSaver = run {
        val accountKey = "userAccount"
        val passwordKey = "userPassword"
        mapSaver (
            save = {
                mapOf(accountKey to userAccount, passwordKey to userPassword)
            },
            restore = {
                userAccount = it[accountKey] as String
                userPassword = it[passwordKey] as String
            })
    }
    rememberSaveable(userAccount, userPassword, saver = accountSaver) { }

    if (isLogin) {
        navCtrl.back()
    }

    //SnackBar弹窗显示信息
    if (errorMessage!=null) {
        coroutineState.launch {
            scaffoldState
                .snackbarHostState
                .showSnackbar(
                    actionLabel = SNACK_ERROR,
                    message = errorMessage!!
                )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HamTheme.colors.themeUi)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        keyboardController?.hide()
                    }
                )
            },
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .fillMaxWidth()
                    .height(ToolBarHeight)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = HamTheme.colors.mainColor,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterStart)
                        .clickable { navCtrl.back() }
                )
                TextContent(
                    text = "用户注册",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            RouteUtils.navTo(navCtrl, RouteName.REGISTER)
                        },
                    color = HamTheme.colors.mainColor
                )
            }
        }
        item {
            Box(Modifier.fillMaxWidth()) {
                LargeTitle(
                    title = "WanAndroid",
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .align(Alignment.Center),
                    color = HamTheme.colors.mainColor
                )
            }
        }
        item {
            LoginEditView(
                text = userAccount,
                labelText = "账号",
                hintText = "请输入用户名",
                onValueChanged = { userAccount = it },
                onDeleteClick = { userAccount = "" }
            )
        }
        item {
            LoginEditView(
                text = userPassword,
                labelText = "密码",
                hintText = "请输入密码",
                onValueChanged = { userPassword = it },
                onDeleteClick = { userPassword = "" },
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                isPassword = true
            )
        }
        item {
            HamButton(
                text = "登录",
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                keyboardController?.hide()
                viewModel.errorMessage.value = null
                viewModel.login(userAccount.trim(), userPassword.trim())
            }
        }
    }
}
