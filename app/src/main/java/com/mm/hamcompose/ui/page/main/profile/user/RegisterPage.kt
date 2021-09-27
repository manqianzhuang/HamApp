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
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: UserViewModel = hiltViewModel()
) {

    val registerSuccess by remember { viewModel.isRegister }
    var userAccount by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage by remember { viewModel.errorMessage }
    val coroutineState = rememberCoroutineScope()


    if (registerSuccess) {
        navCtrl.back()
    }

    //SnackBar弹窗显示信息
    if (errorMessage!=null) {
        popupSnackBar(coroutineState, scaffoldState, label = SNACK_ERROR, errorMessage!!)
        viewModel.errorMessage.value = null
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
            Box(Modifier
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
                MediumTitle(
                    title = "用户注册",
                    modifier = Modifier.align(Alignment.Center),
                    //color = HamTheme.colors.mainColor
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
            LoginEditView(
                text = confirmPassword,
                labelText = "再次输入密码",
                hintText = "请再次输入密码",
                onValueChanged = { confirmPassword = it },
                onDeleteClick = { confirmPassword = "" },
                modifier = Modifier.padding(bottom = 20.dp),
                isPassword = true
            )
        }
        item {
            LoginEditView(
                text = inviteCode,
                labelText = "输入邀请码",
                hintText = "请输入邀请码",
                onValueChanged = { inviteCode = it },
                onDeleteClick = { inviteCode = "" },
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
        item {
            HamButton(
                text = "去注册",
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                keyboardController?.hide()
                val isVerifyAllRight = checkInputsAreNotNull(userAccount, userPassword, confirmPassword, inviteCode)
                if (isVerifyAllRight) {
                    viewModel.register(userAccount.trim(), userPassword.trim(), confirmPassword.trim())
                }
            }
        }
    }
}

private fun checkInputsAreNotNull(
    account: String,
    password: String,
    repassword: String,
    inviteCode: String
): Boolean {
    return account.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty() && inviteCode.isNotEmpty()
}