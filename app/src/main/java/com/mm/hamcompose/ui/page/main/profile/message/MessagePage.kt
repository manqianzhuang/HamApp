package com.mm.hamcompose.ui.page.main.profile.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.EmptyView
import com.mm.hamcompose.ui.widget.HamToolBar
import com.mm.hamcompose.ui.widget.SwitchTabBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MessagePage(
    navCtrl: NavHostController,
    viewModel: MessageViewModel = hiltViewModel()
) {

    val titles by remember { viewModel.titles }
    val tabIndex by remember { viewModel.tabIndex }
    val unreadMessages = viewModel.pagingUnread.value?.collectAsLazyPagingItems()
    val readedMessages = viewModel.pagingReaded.value?.collectAsLazyPagingItems()

    Column {

        HamToolBar(title = "我的消息", onBack = { navCtrl.back() })

        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            pageCount = titles.size,
            initialPage = tabIndex,
            initialOffscreenLimit = titles.size
        )

        SwitchTabBar(
            titles = titles,
            selectIndex = tabIndex,
        ) {
            viewModel.tabIndex.value = it
            coroutineScope.launch {
                pagerState.scrollToPage(tabIndex)
            }
        }

        HorizontalPager(state = pagerState) { page ->
            viewModel.tabIndex.value = pagerState.currentPage
            when (page) {
                0 -> MessageScreen(unreadMessages, false) {
                    viewModel.refreshUnreadData()
                }
                1 -> MessageScreen(readedMessages, true) {
                    viewModel.refreshReadedData()
                }
            }
        }
    }
}

@Composable
private fun MessageScreen(data: LazyPagingItems<Any>?, isReaded: Boolean, onRefresh: ()-> Unit) {
    if (data == null) {
        EmptyView(
            tips = if (isReaded) "没有已读消息" else "没有未读消息",
            imageVector = Icons.Default.Refresh,
            onClick = onRefresh)
    } else {
        LazyColumn {
            //TODO 未知消息的数据json
        }
    }
}