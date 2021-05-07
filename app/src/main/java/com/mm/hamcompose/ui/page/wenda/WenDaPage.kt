package com.mm.hamcompose.ui.page.wenda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.Teal200
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.RouteActions

@Composable
fun WenDaPage(
    actions: RouteActions,
    viewModel: WenDaViewModel = viewModel(WenDaViewModel::class.java)
) {
    viewModel.start()
    val homeData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val refreshing = viewModel.isRefreshing.observeAsState()
    val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(top = 10.dp)
        ) {
            if (homeData != null) {
                items(homeData) { indexBean ->
                    WenDaItem(indexBean!!, actions = actions)
                }
            }
        }
    }
}

@Composable
fun WenDaItem(project: Article, actions: RouteActions) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                val webData = WebData(project.title, project.link!!)
                actions.selected(HamRouter.webView, webData)
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Teal200)
                    .padding(20.dp)
            ) {
                Column {
                    //标题
                    Text(
                        text = project.title!!,
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = white1,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        //textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(top = 5.dp)
                    ) {

                        Text(
                            text = "作者：${project.author!!}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = HamTheme.colors.background,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        //发布时间
                        Text(
                            text = "发布时间：${project.niceDate!!}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = HamTheme.colors.background,
                            //modifier = Modifier.width(80.dp),
                            maxLines = 1,
                        )

                    }
                }
            }

            Text(
                text = project.desc!!,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 7,
                overflow = TextOverflow.Ellipsis,
                color = HamTheme.colors.textSecondary,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}
