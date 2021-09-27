package com.mm.hamcompose.ui.page.main.profile.points

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.theme.H5
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PointsRankingPage(
    navCtrl: NavHostController,
    viewModel: PointsRankingViewModel = hiltViewModel()
) {

    viewModel.start()
    val titles by remember { viewModel.titles }
    val rankings = viewModel.pagingRanking.value?.collectAsLazyPagingItems()
    val isLoaded = rankings?.loadState?.prepend?.endOfPaginationReached ?: false
    val records = viewModel.pagingRecords.value?.collectAsLazyPagingItems()
    val personPoints by remember { viewModel.personalPoints }
    val tabIndex by remember { viewModel.tabIndex }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = HamTheme.colors.themeUi)
    ) {

        HamToolBar(title = "积分排行", onBack = { navCtrl.back() })

        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            pageCount = titles.size,
            initialPage = tabIndex,
            initialOffscreenLimit = titles.size
        )

        SwitchTabBar(titles = titles, selectIndex = tabIndex, heightValue = 30.dp) {
            viewModel.tabIndex.value = it
            coroutineScope.launch {
                pagerState.scrollToPage(tabIndex)
            }
        }

        HorizontalPager(state = pagerState) { page ->
            viewModel.tabIndex.value = pagerState.currentPage
            when (page) {
                0 -> RankingScreen(isLoaded, rankings, personPoints, navCtrl)
                1 -> RecordsScreen(records, personPoints?.coinCount)
            }
        }
    }
}

@Composable
private fun RankingScreen(
    isLoaded: Boolean,
    rankings: LazyPagingItems<PointsBean>?,
    person: PointsBean?,
    navCtrl: NavHostController
) {

    Column(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .fillMaxSize()
            .background(
                color = HamTheme.colors.mainColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            MediumTitle(
                title = "用户",
                modifier = Modifier.weight(1f),
            )
            MediumTitle(
                title = "积分",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            MediumTitle(
                title = "排名",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Divider(
            thickness = 0.5.dp,
            modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
        )

        if (person != null) {
            Row(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                TextContent(
                    text = person.username,
                    modifier = Modifier.weight(1f),
                    color = HamTheme.colors.themeUi,
                    maxLines = 1,
                )
                TextContent(
                    text = person.coinCount,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = HamTheme.colors.themeUi,
                    maxLines = 1,
                )
                TextContent(
                    text = person.rank!!,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = HamTheme.colors.themeUi,
                    maxLines = 1,
                )
            }
        }

        if (!isLoaded) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = HamTheme.colors.themeUi
                )
            }
        } else {
            if (rankings != null) {
                LazyColumn {
                    itemsIndexed(rankings) { index, rank ->
                        Row(
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .clickable {
                                    RouteUtils.navTo(navCtrl, RouteName.SHARER, rank?.userId)
                                }
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                            ) {
                                TextContent(
                                    text = rank?.username ?: "username",
                                    color = if (index < 3) HamTheme.colors.textPrimary else HamTheme.colors.textSecondary,
                                    maxLines = 1,
                                )
                                if (index < 3) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_hot),
                                        contentDescription = null,
                                        tint = HamTheme.colors.hot,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            TextContent(
                                text = rank?.coinCount ?: "points",
                                textAlign = TextAlign.Center,
                                color = if (index < 3) HamTheme.colors.textPrimary else HamTheme.colors.textSecondary,
                                maxLines = 1,
                            )
                            TextContent(
                                text = rank?.rank ?: "ranking",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                                color = if (index < 3) HamTheme.colors.textPrimary else HamTheme.colors.textSecondary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            else {
                EmptyView()
            }
        }


    }
}

@Composable
private fun RecordsScreen(
    records: LazyPagingItems<PointsBean>?,
    points: String?,
) {

    Column(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .fillMaxSize()
            .background(
                color = HamTheme.colors.mainColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            MediumTitle(title = "积分详情")
            MiniTitle(
                text = "合计：${points ?: 0}",
                Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.Bottom)
            )
        }

        if (records == null) {
            return
        }

        LazyColumn {
            items(records) { record ->
                Column {
                    Row {
                        TextContent(
                            text = record?.desc!!,
                            modifier = Modifier.weight(1f),
                            color = when (record.type) {
                                1 -> HamTheme.colors.info
                                2 -> HamTheme.colors.success
                                else -> HamTheme.colors.hot
                            }
                        )
                        TextContent(
                            text = record.reason!!,
                            color = when (record.type) {
                                1 -> HamTheme.colors.info
                                2 -> HamTheme.colors.success
                                else -> HamTheme.colors.hot
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        )

                    }
                    Divider(
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
                    )

                }
            }

        }

    }
}
