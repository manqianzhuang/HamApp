package com.mm.hamcompose.ui.page.main.profile.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.SPUtils
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.THEME_COLOR_KEY
import com.mm.hamcompose.theme.themeColors
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*
import com.mm.hamcompose.util.CacheDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SettingsPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    //onThemeSelect: (color: Color)-> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    var clickExitApp by remember { mutableStateOf(false) }
    var logout by remember { viewModel.logout }
    var isClearCache by remember { mutableStateOf(false) }
    var isAboutMe by remember { mutableStateOf(false) }
    var clickPalette by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var cacheSize by remember { mutableStateOf("") }
    var tipsMessage by remember { mutableStateOf("") }

    val themeIndex by remember { viewModel.themeIndex }
    val selectTheme by remember { viewModel.selectTheme }

    if (selectTheme!=null) {
        HamTheme.colors.themeUi = selectTheme!!
        HamTheme.colors.primaryBtnBg = selectTheme!!
        viewModel.selectTheme.value = null
    }

    if (logout) {
        println("SettingsPage ===>  logout")
        navCtrl.popBackStack()
        viewModel.logout.value = false
    }

    coroutineScope.launch {
        val size = withContext(Dispatchers.IO) {
            CacheDataManager.getTotalCacheSize(context)
        }
        cacheSize = withContext(Dispatchers.Main) { size }
    }

    if (tipsMessage.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, tipsMessage)
        tipsMessage = ""
    }

    Column(Modifier.fillMaxSize()) {
        HamToolBar(title = "设置", onBack = { navCtrl.back() })
        LazyColumn {
            item {
                ArrowRightListItem(
                    iconRes = painterResource(R.drawable.ic_theme),
                    title = "主题更换",
                ) {
                    clickPalette = true
                }
                ArrowRightListItem(
                    iconRes = painterResource(R.drawable.ic_message),
                    title = "清理缓存",
                    valueText = cacheSize
                ) {
                    isClearCache = true
                }
                ArrowRightListItem(
                    iconRes = painterResource(R.drawable.ic_help),
                    title = "关于我"
                ) {
                    isAboutMe = true
                }
                ArrowRightListItem(
                    iconRes = painterResource(R.drawable.ic_message),
                    title = "退出登录",
                ) {
                    clickExitApp = true
                }
            }
        }
    }

    if (clickPalette) {
        PaletteSelectorDialog(
            initKey = themeIndex,
            onDismiss ={
                clickPalette = false
            },
            onSelectItem = {
                viewModel.themeIndex.value = it
                viewModel.selectTheme.value = themeColors[it]
            }
        )
    }

    if (clickExitApp) {
        SampleAlertDialog(
            title = "提示",
            content = "退出后，将无法查看我的文章、消息、收藏、积分、浏览记录等功能，确定退出登录吗？",
            onConfirmClick = {
                viewModel.logout()
            },
            onDismiss = {
                clickExitApp = false
            }
        )
    }

    if (isAboutMe) {
        InfoDialog(
            content = arrayOf(
                "作者: SuperMAN",
                "email: ganzhuangman@gmail.com",
                "版本号: v1.0",
                "项目源码: https://github.com/manqianzhuang/HamApp.git",
                "版权声明: 本app仅用于学习用处，不得抄袭用于商业行为",
            ),
            onDismiss = {
                isAboutMe = false
            }
        )
    }

    if (isClearCache) {
        SampleAlertDialog(
            title = "提示",
            content = "清除缓存后，缓存文件夹中的照片或文件可能丢失，确定清除吗？",
            onConfirmClick = {
                coroutineScope.launch {
                    val cacheResult = withContext(Dispatchers.IO) {
                        CacheDataManager.clearAllCache(context)
                        val size = CacheDataManager.getTotalCacheSize(context)
                        arrayOf("缓存已清理", size)
                    }
                    withContext(Dispatchers.Main) {
                        isClearCache = false
                        tipsMessage = cacheResult[0]
                        cacheSize = cacheResult[1]
                    }
                }
            },
            onDismiss = {
                isClearCache = false
            }
        )
    }
}

