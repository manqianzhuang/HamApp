package com.mm.hamcompose.ui.page.base

import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.gson.Gson
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.data.bean.WelfareData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.page.girls.info.GirlInfoPage
import com.mm.hamcompose.ui.page.girls.list.GirlPhotoPage
import com.mm.hamcompose.ui.page.main.category.CategoryPage
import com.mm.hamcompose.ui.page.main.category.pubaccount.author.PublicAccountAuthorPage
import com.mm.hamcompose.ui.page.main.category.pubaccount.search.PublicAccountSearch
import com.mm.hamcompose.ui.page.main.category.share.ShareArticlePage
import com.mm.hamcompose.ui.page.main.category.structure.list.StructureListPage
import com.mm.hamcompose.ui.page.main.collection.CollectionPage
import com.mm.hamcompose.ui.page.main.collection.edit.WebSiteEditPage
import com.mm.hamcompose.ui.page.main.home.HomePage
import com.mm.hamcompose.ui.page.main.home.search.SearchPage
import com.mm.hamcompose.ui.page.main.profile.ProfilePage
import com.mm.hamcompose.ui.page.main.profile.history.HistoryPage
import com.mm.hamcompose.ui.page.main.profile.message.MessagePage
import com.mm.hamcompose.ui.page.main.profile.points.PointsRankingPage
import com.mm.hamcompose.ui.page.main.profile.settings.SettingsPage
import com.mm.hamcompose.ui.page.main.profile.sharer.SharerPage
import com.mm.hamcompose.ui.page.main.profile.user.LoginPage
import com.mm.hamcompose.ui.page.main.profile.user.RegisterPage
import com.mm.hamcompose.ui.page.webview.WebViewPage
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.BottomNavBarView
import com.mm.hamcompose.ui.widget.HamSnackBar

private inline fun <reified T> jsonToObject(json: String?): T? {
    return runCatching {
        Gson().fromJson(json, T::class.java)
    }.onFailure {
        println("json parse ERROR = ${it.message}")
    }.getOrNull()
}

@Composable
fun HamScaffold() {

    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.HOME -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.CATEGORY -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.COLLECTION -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.PROFILE -> BottomNavBarView(navCtrl = navCtrl)
            }
        },
        content = {
            var homeIndex = remember { 0 }
            var categoryIndex = remember { 0 }

            NavHost(
                modifier = Modifier.background(HamTheme.colors.background),
                navController = navCtrl,
                startDestination = RouteName.HOME
            ) {
                //??????
                composable(route = RouteName.HOME) {
                    HomePage(navCtrl, scaffoldState, homeIndex) { homeIndex = it }
                }

                //??????
                composable(route = RouteName.CATEGORY) {
                    CategoryPage(navCtrl, categoryIndex) { categoryIndex = it }
                }

                //??????
                composable(route = RouteName.COLLECTION) {
                    CollectionPage(navCtrl, scaffoldState)
                }

                //??????
                composable(route = RouteName.PROFILE) {
                    ProfilePage(navCtrl)
                }

                //???????????????
                composable(
                    route = RouteName.ARTICLE_SEARCH + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) {
                    SearchPage(navCtrl, scaffoldState)
                }

                //?????????
                composable(route = RouteName.GIRL_PHOTO) {
                    GirlPhotoPage(navCtrl)
                }

                //?????????(??????)
                composable(route = RouteName.GIRL_INFO) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    if (args != null && args is WelfareData) {
                        GirlInfoPage(
                            welfare = args,
                            navCtrl = navCtrl,
                            scaffoldState = scaffoldState
                        )
                    }
                }

                //???????????????
                composable(route = RouteName.PUB_ACCOUNT_DETAIL) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    if (args != null && args is ParentBean) {
                        PublicAccountAuthorPage(
                            parent = args,
                            navCtrl = navCtrl,
                            scaffoldState = scaffoldState
                        )
                    }

                }

                //??????
                composable(route = RouteName.STRUCTURE_LIST) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    if (args != null && args is ParentBean) {
                        StructureListPage(
                            parent = args,
                            navCtrl = navCtrl,
                            scaffoldState = scaffoldState
                        )
                    }

                }

                //???????????????
                composable(route = RouteName.PUB_ACCOUNT_SEARCH) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    if (args != null && args is ParentBean) {
                        PublicAccountSearch(
                            parent = args,
                            navCtrl = navCtrl,
                            scaffoldState = scaffoldState
                        )
                    }
                }

                //WebView
                composable(route = RouteName.WEB_VIEW) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    if (args != null && args is WebData) {
                        WebViewPage(webData = args, navCtrl = navCtrl)
                    }
                }

                //??????
                composable(route = RouteName.LOGIN) {
                    LoginPage(navCtrl, scaffoldState)
                }

                //??????
                composable(route = RouteName.REGISTER) {
                    RegisterPage(navCtrl, scaffoldState)
                }

                //???????????????
                composable(route = RouteName.RANKING) {
                    PointsRankingPage(navCtrl)
                }

                //??????
                composable(route = RouteName.MESSAGE) {
                    MessagePage(navCtrl)
                }

                //??????
                composable(route = RouteName.SETTINGS) {
                    SettingsPage(
                        navCtrl = navCtrl,
                        scaffoldState = scaffoldState
                    )
                }

                //????????????
                composable(route = RouteName.EDIT_WEBSITE) {
                    val args = RouteUtils.getArguments<Any>(navCtrl)
                    WebSiteEditPage(
                        website = if (args != null && args is ParentBean) args else null,
                        navCtrl = navCtrl,
                        scaffoldState = scaffoldState
                    )
                }

                // ??????/???????????????????????????
                composable(
                    route = RouteName.SHARER + "/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.IntType })
                ) {
                    SharerPage(
                        userId = it.arguments?.getInt("userId"),
                        navCtrl = navCtrl,
                        scaffoldState = scaffoldState
                    )
                }

                //????????????
                composable(route = RouteName.SHARE_ARTICLE) {
                    ShareArticlePage(
                        navCtrl = navCtrl,
                        scaffoldState = scaffoldState
                    )
                }

                composable(route = RouteName.HISTORY) {
                    HistoryPage(navCtrl = navCtrl, scaffoldState = scaffoldState)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                println("actionLabel = ${data.actionLabel}")
                HamSnackBar(data = data)
            }
        }
    )
}

