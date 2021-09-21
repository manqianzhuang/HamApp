package com.mm.hamcompose.ui.page.main.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mm.hamcompose.theme.BottomNavBarHeight
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.page.main.category.navigation.NaviPage
import com.mm.hamcompose.ui.page.main.category.pubaccount.category.PublicAccountPage
import com.mm.hamcompose.ui.page.main.category.structure.tree.StructurePage
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.TextTabBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPage(
    navCtrl: NavHostController,
    categoryIndex: Int = 0,
    viewModel: CategoryViewModel = hiltViewModel(),
    onPageSelected: (position: Int) -> Unit,
) {

    val titles by remember { viewModel.titles }
    Box(modifier = Modifier.padding(bottom = BottomNavBarHeight)) {
        Column {
            val pagerState = rememberPagerState(
                pageCount = titles.size,
                initialPage = categoryIndex,
                initialOffscreenLimit = titles.size
            )
            val scopeState = rememberCoroutineScope()

            Row {
                TextTabBar(
                    index = pagerState.currentPage,
                    tabTexts = titles,
                    modifier = Modifier.weight(1f),
                    onTabSelected = { index ->
                        scopeState.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    withAdd = true,
                    onAddClick = {
                        RouteUtils.navTo(navCtrl, RouteName.SHARE_ARTICLE)
                    }
                )
            }

            HorizontalPager(state = pagerState) { page ->
                onPageSelected(pagerState.currentPage)
                when (page) {
                    0 -> StructurePage(navCtrl)
                    1 -> NaviPage(navCtrl)
                    2 -> PublicAccountPage(navCtrl)
                }
            }
        }
    }
}
