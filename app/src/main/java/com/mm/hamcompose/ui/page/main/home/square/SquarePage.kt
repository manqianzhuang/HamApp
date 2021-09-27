package com.mm.hamcompose.ui.page.main.home.square

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.EmptyView
import com.mm.hamcompose.ui.widget.MultiStateItemView
import com.mm.hamcompose.ui.widget.SNACK_INFO
import com.mm.hamcompose.ui.widget.popupSnackBar

@Composable
fun SquarePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SquareViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int) -> Unit,
) {

    viewModel.start()
    val squareData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val isLoaded = squareData?.loadState?.prepend?.endOfPaginationReached ?: false
    val refreshing: Boolean by remember { viewModel.isRefreshing }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val currentPosition by remember { viewModel.currentListIndex }
    val message by remember { viewModel.message }
    val listState = rememberLazyListState(currentPosition)
    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }


    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() }
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(top = 10.dp)
        ) {

            if (isLoaded) {
                if (squareData!!.itemCount > 0) {
                    itemsIndexed(squareData) { index, item ->
                        MultiStateItemView(
                            data = item!!,
                            onSelected = {
                                viewModel.saveDataToHistory(item)
                                viewModel.savePosition(listState.firstVisibleItemIndex)
                                RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, it)
                            },
                            onCollectClick = {
                                if (item.collect) {
                                    viewModel.uncollectArticleById(it)
                                    squareData.peek(index)?.collect = false
                                } else {
                                    viewModel.collectArticleById(it)
                                    squareData.peek(index)?.collect = true
                                }

                            },
                            onUserClick = { userId ->
                                RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                            })
                    }
                    onScrollChangeListener(listState.firstVisibleItemIndex)
                } else {
                    item { EmptyView() }
                }
            } else {
                items(5) {
                    MultiStateItemView(
                        data = Article(),
                        isLoading = true
                    )
                }
            }

        }
    }
}