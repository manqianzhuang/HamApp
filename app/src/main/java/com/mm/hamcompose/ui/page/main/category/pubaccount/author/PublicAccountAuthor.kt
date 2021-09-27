package com.mm.hamcompose.ui.page.main.category.pubaccount.author

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ToolBarHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*

@Composable
fun PublicAccountAuthorPage(
    parent: ParentBean?,
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: PublicAccountAuthorViewModel = hiltViewModel()
) {

    parent ?: return
    viewModel.setPublicId(parent.id)
    viewModel.start()

    val articles = viewModel.publicData.value?.collectAsLazyPagingItems()
    val isLoaded = articles?.loadState?.prepend?.endOfPaginationReached ?: false
    val refreshing by remember { viewModel.isRefreshing }
    val message by remember { viewModel.message }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }

    Column(
        modifier = Modifier.background(HamTheme.colors.background)
    ) {
        HamToolBar(
            title = parent.name ?: "",
            onBack = {
                navCtrl.back()
            },
            onRightClick = {
                RouteUtils.navTo(navCtrl, RouteName.PUB_ACCOUNT_SEARCH, parent)
            },
            imageVector = Icons.Default.Search
        )

        SwipeRefresh(
            state = swipeRefreshState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onRefresh = {
                viewModel.savePosition(0)
                viewModel.clearCache()
                viewModel.initPublicArticles()
            },
        ) {
            LazyColumn(state = listState) {
                if (isLoaded) {
                    if (articles!!.itemCount > 0) {
                        items(articles) { item ->
                            SimpleListItemView(
                                data = item!!,
                                onClick = {
                                    viewModel.saveDataToHistory(item)
                                    viewModel.savePosition(listState.firstVisibleItemIndex)
                                    RouteUtils.navTo(
                                        navCtrl,
                                        RouteName.WEB_VIEW,
                                        WebData(item.title, item.link!!)
                                    )
                                },
                                onCollectClick = {
                                    if (item.collect) {
                                        viewModel.uncollectArticleById(it)
                                        item.collect = false
                                    } else {
                                        viewModel.collectArticleById(it)
                                        item.collect = true
                                    }
                                }
                            )
                        }
                    } else {
                        item { EmptyView() }
                    }
                } else {
                    items(6) {
                        SimpleListItemView(
                            data = Article(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}
