package com.mm.hamcompose.ui.page.main.home.index

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IndexPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: IndexViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int) -> Unit,
) {
    viewModel.start()
    val homeData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val isLoaded = homeData?.loadState?.prepend?.endOfPaginationReached ?: false
    val message by remember { viewModel.message }
    val banners by remember { viewModel.imageList }
    val refreshing by remember { viewModel.isRefreshing }
    val topArticle = remember { viewModel.topArticles }
    val currentPosition by remember { viewModel.currentListIndex }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentPosition)
    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.refresh()
        }
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
                        modifier = Modifier.padding(top = if (index == 0) 5.dp else 0.dp),
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
                                viewModel.topArticles[index].collect = false
                            } else {
                                viewModel.collectArticleById(it)
                                viewModel.topArticles[index].collect = true
                            }
                        },
                        onUserClick = { userId ->
                            RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                        },
                    )
                }
            }

            if (!isLoaded) {
                items(5) {
                    MultiStateItemView(
                        data = Article(),
                        isLoading = true
                    )
                }
            } else {
                if (homeData != null && homeData.itemCount > 0) {
                    itemsIndexed(homeData) { index, item ->
                        MultiStateItemView(
                            data = item!!,
                            onSelected = { data ->
                                viewModel.saveDataToHistory(item)
                                viewModel.savePosition(listState.firstVisibleItemIndex)
                                RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, data)
                            },
                            onCollectClick = {
                                if (item.collect) {
                                    viewModel.uncollectArticleById(it)
                                    homeData.peek(index)?.collect = false
                                } else {
                                    viewModel.collectArticleById(it)
                                    homeData.peek(index)?.collect = true
                                }
                            },
                            onUserClick = { userId ->
                                RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                            }
                        )
                    }
                }
                else {
                    item{ EmptyView() }
                }
            }

            onScrollChangeListener(listState.firstVisibleItemIndex)
        }
    }
}

