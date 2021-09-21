package com.mm.hamcompose.ui.page.main.home.index

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.Banner
import com.mm.hamcompose.ui.widget.MultiStateItemView

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IndexPage(
    navCtrl: NavHostController,
    viewModel: IndexViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int)-> Unit,
) {
    viewModel.start()
    val homeData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val banners by remember { viewModel.imageList }
    val refreshing by remember { viewModel.isRefreshing }
    val topArticle by remember { viewModel.topArticles }
    val currentPosition by remember { viewModel.currentListIndex }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val listState = rememberLazyListState(currentPosition)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {

            if (banners.isNotEmpty()) {
                item {
                    Banner(list = banners) { url, title ->
                        RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, WebData(title, url))
                    }
                }
            }

            if (topArticle.isNotEmpty()) {
                itemsIndexed(topArticle) { index, item ->
                    MultiStateItemView(
                        modifier = Modifier.padding(top = if (index==0) 5.dp else 0.dp),
                        data = item,
                        isTop = true,
                        onSelected = { data ->
                            viewModel.saveDataToHistory(item)
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, data)
                        },
                        onCollectClick = {
                            if (item.collect) {
                                viewModel.uncollectArticleById(it)
                                item.collect = false
                            } else {
                                viewModel.collectArticleById(it)
                                item.collect = true
                            }
                        },
                        onUserClick = { userId ->
                            RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                        }
                    )
                }
            }
            if (homeData != null) {
                items(homeData) { item ->
                    MultiStateItemView(
                        data = item!!,
                        onSelected = { data ->
                            viewModel.saveDataToHistory(item)
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, data)
                        },
                        onCollectClick = {
                            viewModel.collectArticleById(it)
                        },
                        onUserClick = { userId ->
                            RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                        }
                    )
                }
            }
            onScrollChangeListener(listState.firstVisibleItemIndex)
        }
    }
}

