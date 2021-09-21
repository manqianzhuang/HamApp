package com.mm.hamcompose.ui.page.main.home.wenda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.MainTitle
import com.mm.hamcompose.ui.widget.MiniTitle
import com.mm.hamcompose.ui.widget.TextContent
import com.mm.hamcompose.util.RegexUtils

@Composable
fun WenDaPage(
    navCtrl: NavHostController,
    viewModel: WenDaViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int)-> Unit,
) {
    viewModel.start()
    val wendaData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val refreshing by remember { viewModel.isRefreshing }
    var currentPosition by remember { viewModel.currentListIndex }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val listState = rememberLazyListState(currentPosition)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            state = listState,
            contentPadding = PaddingValues(vertical = 5.dp)
        ) {
            if (wendaData != null) {
                itemsIndexed(wendaData) { position, wenda ->
                    WenDaItem(wenda!!) {
                        currentPosition = position
                        val webData = WebData(wenda.title, wenda.link!!)
                        RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, webData)
                    }
                }
                onScrollChangeListener(listState.firstVisibleItemIndex)
            }
        }
    }
}

@Composable
fun WenDaItem(article: Article, onClick: ()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clickable {
                onClick.invoke()
            }
    ) {
        Column {

            //标题
            MainTitle(
                title = article.title ?: "标题",
                maxLine = 2,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
            )
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 5.dp, bottom = 10.dp)
            ) {

                MiniTitle(
                    text = "作者：${article.author ?: "xxx"}",
                    color = HamTheme.colors.textSecondary,
                    modifier = Modifier.padding(end = 10.dp),

                )
                //发布时间
                MiniTitle(
                    text = "发布时间：${article.niceDate ?: "1970-1-1"}",
                    color = HamTheme.colors.textSecondary,
                    maxLines = 1,
                )
            }

            TextContent(
                text = RegexUtils().symbolClear(article.desc),
                maxLines = 3,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}

