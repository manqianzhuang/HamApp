package com.mm.hamcompose.ui.page.square

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.index.IndexItem

@Composable
fun SquarePage(
    actions: RouteActions,
    viewModel: SquareViewModel = viewModel(SquareViewModel::class.java)
) {

    viewModel.start()
    val homeData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val refreshing = viewModel.isRefreshing.observeAsState()
    val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        LogUtils.e("刷新数据")
        viewModel.refresh()
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(top = 10.dp)
        ) {
            if (homeData != null) {
                items(homeData) { indexBean ->
                    IndexItem(indexBean!!, onSelected = actions.selected)
                }
            }
        }
    }
}