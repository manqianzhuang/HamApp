package com.mm.hamcompose.ui.page.main.home.wenda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*
import com.mm.hamcompose.util.RegexUtils

@Composable
fun WenDaPage(
    navCtrl: NavHostController,
    viewModel: WenDaViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int) -> Unit,
) {
    viewModel.start()
    val wendaData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val isLoaded = wendaData?.loadState?.prepend?.endOfPaginationReached ?: false
    val refreshing by remember { viewModel.isRefreshing }
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
            contentPadding = PaddingValues(vertical = 5.dp)
        ) {
            if (isLoaded) {
                if (wendaData != null && wendaData.itemCount > 0) {
                    items(wendaData) { wenda ->
                        WenDaItem(wenda!!) {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            wenda.run {
                                RouteUtils.navTo(
                                    navCtrl,
                                    RouteName.WEB_VIEW,
                                    WebData(title, link!!)
                                )
                            }
                        }
                    }
                    onScrollChangeListener(listState.firstVisibleItemIndex)
                } else {
                    item { EmptyView() }
                }
            } else {
                items(5) {
                    WenDaItem(article = Article(), isLoading = true)
                }
            }
        }
    }
}

@Composable
private fun WenDaItem(article: Article, isLoading: Boolean = false, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clickable(enabled = !isLoading) {
                onClick.invoke()
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            //标题
            MainTitle(
                title = titleSubstring(article.title) ?: "每日一问",
                maxLine = 2,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp)
            ) {

                //UserIcon(isLoading = isLoading)
                MiniTitle(
                    text = "作者:${article.author ?: "xxx"}",
                    color = HamTheme.colors.textSecondary,
                    modifier = Modifier
                        .padding(start = if (isLoading) 5.dp else 0.dp)
                        .align(Alignment.CenterVertically),
                    isLoading = isLoading
                )
                Spacer(modifier = Modifier.width(10.dp))
                //发布时间
                //TimerIcon(isLoading = isLoading)
                MiniTitle(
                    modifier = Modifier
                        .padding(start = if (isLoading) 5.dp else 0.dp)
                        .align(Alignment.CenterVertically),
                    text = "日期:${RegexUtils().timestamp(article.niceDate) ?: "2020"}",
                    color = HamTheme.colors.textSecondary,
                    maxLines = 1,
                    isLoading = isLoading
                )
            }

            TextContent(
                text = RegexUtils().symbolClear(article.desc),
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp),
                isLoading = isLoading
            )
        }
    }
}

private fun titleSubstring(oldText: String?): String? {
    return oldText?.run {
        var newText = this
        if (startsWith("每日一问") && contains(" | ")) {
            newText = substring(indexOf(" | ")+3, length)
        }
        newText
    }
}

