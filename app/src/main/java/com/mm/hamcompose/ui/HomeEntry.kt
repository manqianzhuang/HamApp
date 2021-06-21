package com.mm.hamcompose.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.page.girls.GirlPhotoPage
import com.mm.hamcompose.ui.page.home.HomePage
import com.mm.hamcompose.ui.page.project.category.ProjectDetailPage
import com.mm.hamcompose.ui.page.search.SearchPage
import com.mm.hamcompose.ui.page.splash.SplashPage
import com.mm.hamcompose.ui.page.subscription.author.SubscriptionAuthorPage
import com.mm.hamcompose.ui.page.subscription.search.SubscriptionSearch
import com.mm.hamcompose.ui.page.system.category.SystemArticlePage
import com.mm.hamcompose.ui.page.webview.WebViewPage
import com.mm.hamcompose.util.Navigator

@Composable
fun HomeEntry(backDispatcher: OnBackPressedDispatcher) {
    //记录当前的状态
    val navigator: Navigator<Destination> = rememberSaveable(
        saver = Navigator.saver(backDispatcher)
    ) {
        //设定默认的栈底
        Navigator(Destination.Home, backDispatcher)
    }
    //跳转栈or回退栈 的操作
    val actions = remember(navigator) { RouteActions(navigator) }
    var homeIndex = remember { 0 }

    var isSplash by remember { mutableStateOf(true) }

    if (isSplash) {
        SplashPage { isSplash = false }
    } else {
        HamTheme {
            LogUtils.e("homeIndex = $homeIndex")
            Crossfade(navigator.current) { destination ->
                when (destination) {
                    Destination.Home -> HomePage(homeIndex, { homeIndex = it }, actions)
                    Destination.ArticleSearch -> SearchPage(actions)
                    Destination.GirlPhoto -> GirlPhotoPage(actions)
                    is Destination.ProjectDetail -> ProjectDetailPage(destination.parent, actions)
                    is Destination.SubscribeDetail -> SubscriptionAuthorPage(destination.parent, actions)
                    is Destination.SystemCategory -> SystemArticlePage(destination.parent, actions)
                    is Destination.SubscribeSearch -> SubscriptionSearch(destination.parent, actions)
                    is Destination.WebViewBrowser -> WebViewPage(destination.webData, actions.backPress)
                }
            }
        }
    }

}

