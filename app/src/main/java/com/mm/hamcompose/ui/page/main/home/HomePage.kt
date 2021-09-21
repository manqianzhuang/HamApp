package com.mm.hamcompose.ui.page.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mm.hamcompose.ui.page.main.home.index.IndexPage
import com.mm.hamcompose.ui.page.main.home.project.ProjectPage
import com.mm.hamcompose.ui.page.main.home.square.SquarePage
import com.mm.hamcompose.ui.page.main.home.wenda.WenDaPage
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.HomeSearchBar
import com.mm.hamcompose.ui.widget.TextTabBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navCtrl: NavHostController,
    homeIndex: Int = 0,
    viewModel: HomeViewModel = hiltViewModel(),
    onPageSelected: (position: Int) -> Unit,
) {

    val isShowSearchBar by remember { viewModel.isShowSearchBar }
    val titles by remember { viewModel.titles }
    val scopeState = rememberCoroutineScope()

    Column {
        if (isShowSearchBar) {
            HomeSearchBar(
                onSearchClick = {
                    RouteUtils.navTo(navCtrl, RouteName.ARTICLE_SEARCH + "/111")
                },
                onRightIconClick = {
                    RouteUtils.navTo(navCtrl, RouteName.GIRL_PHOTO)
                }
            )
        }

        val pagerState = rememberPagerState(
            pageCount = titles.size,
            initialPage = homeIndex,
            initialOffscreenLimit = titles.size
        )

        TextTabBar(
            index = pagerState.currentPage,
            tabTexts = titles,
            onTabSelected = { index ->
                scopeState.launch {
                    pagerState.scrollToPage(index)
                }
            }
        )

        HorizontalPager(state = pagerState, dragEnabled = false) { page ->
            onPageSelected(pagerState.currentPage)
            when (page) {
                0 -> IndexPage(navCtrl) { position -> viewModel.setCachePosition(0, position) }
                1 -> SquarePage(navCtrl) { position -> viewModel.setCachePosition(0, position) }
                2 -> ProjectPage(navCtrl) { position -> viewModel.setCachePosition(0, position) }
                3 -> WenDaPage(navCtrl) { position -> viewModel.setCachePosition(0, position) }
            }
        }
    }

}

//@Composable
//fun DrawerMenu(
//    viewModel: HomeViewModel,
//    navCtrl: NavHostController? = null,
//    onItemClick: (pos: Int) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//                .background(HamTheme.colors.themeUi)
//        ) {
//            Column(
//                modifier = Modifier.align(Alignment.Center)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_author),
//                    contentDescription = "用户头像",
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(RoundedCornerShape(20.dp)),
//                    contentScale = ContentScale.FillBounds
//                )
//            }
//        }
//
//        viewModel.menuItems.forEachIndexed { i, item ->
//            DrawerItem(
//                title = item.title,
//                iconRes = item.iconRes,
//                navCtrl = navCtrl,
//                onItemClick = {
//                    onItemClick(i)
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun DrawerItem(
//    title: String,
//    iconRes: Int? = null,
//    isSelect: Boolean = false,
//    navCtrl: NavHostController? = null,
//    onItemClick: () -> Unit,
//) {
//    DropdownMenuItem(
//        onClick = {
//            onItemClick()
//
//        }
//    ) {
//        val tintColor = if (isSelect) HamTheme.colors.themeUi else HamTheme.colors.textSecondary
//        if (iconRes == null) {
//            Icon(
//                imageVector = Icons.Default.Home,
//                contentDescription = "home",
//                modifier = Modifier.size(25.dp),
//                tint = tintColor
//            )
//        } else {
//            Icon(
//                painter = painterResource(id = iconRes),
//                contentDescription = "home",
//                modifier = Modifier.size(25.dp),
//                tint = tintColor
//            )
//        }
//
//        MediumTitle(
//            title = title,
//            modifier = Modifier.padding(start = 5.dp),
//            color = tintColor
//        )
//    }
//}
