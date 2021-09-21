package com.mm.hamcompose.ui.page.main.profile.share

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.MY_USER_ID
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*
import com.mm.hamcompose.util.RegexUtils

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SharerPage(
    userId: Int?,
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SharerViewModel = hiltViewModel()
) {

    if (userId != null) {
        viewModel.setupUserId(userId)
    }
    viewModel.start()

    val errorMessage by remember { viewModel.errorMessage }
    val hasMore by remember { viewModel.hasMore }
    val showLoadingMore by remember { viewModel.isLoadingMore }
    val points by remember { viewModel.points }
    val articles = remember { viewModel.articles }
    val currentPosition by remember { viewModel.currentListIndex }
    var editable by remember { mutableStateOf(false) }
    val listState = rememberLazyListState(currentPosition)

    if (errorMessage != null) {
        popSnack(
            scope = rememberCoroutineScope(),
            scaffoldState = scaffoldState,
            label = SNACK_ERROR,
            message = errorMessage!!
        ) { viewModel.errorMessage.value = null }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //头部栏
        HamToolBar(
            title = points?.username ?: "作者",
            onBack = { navCtrl.back() },
            onRightClick = {
                editable = !editable
            },
            imageVector = if (userId == MY_USER_ID && !articles.isNullOrEmpty()) Icons.Default.Edit else null
        )
        //滚动区域
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState
        ) {
            item { UserInfo(points = points) }
            stickyHeader { ListTitle(title = "文章列表") }
            if (!articles.isNullOrEmpty()) {
                itemsIndexed(articles) { index, article ->
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        TextContent(
                            text = "${index + 1}. ${article.title}",
                            maxLines = 2,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val webData = WebData(article.title, article.link!!)
                                    RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, webData)
                                }
                        )
                        if (editable) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = HamTheme.colors.textSecondary,
                                modifier = Modifier.clickable {
                                    viewModel.deleteMyArticle(article.id)
                                }
                            )
                        }
                    }
                }
                if (hasMore) {
                    if (showLoadingMore) {
                        item {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .height(5.dp)
                                    .fillMaxWidth(),
                                color = HamTheme.colors.themeUi
                            )
                        }
                    }
                    if (listState.firstVisibleItemIndex > articles.size - 20) {
                        viewModel.nextPage()
                    }
                }
            } else {
                item {
                    Box(Modifier.height(400.dp)) {
                        EmptyView(tips = "啥都木有~", Icons.Default.Info)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserInfo(points: PointsBean?) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = HamTheme.colors.mainColor
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(vertical = 15.dp)
            ) {
                MiniTitle(text = "等级", modifier = Modifier.align(Alignment.CenterHorizontally))
                TextContent(
                    text = "${points?.level ?: 0}",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(vertical = 15.dp)
            ) {
                MiniTitle(text = "积分", modifier = Modifier.align(Alignment.CenterHorizontally))
                TextContent(
                    text = "${points?.coinCount ?: 0}",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(vertical = 15.dp)
            ) {
                MiniTitle(text = "排行", modifier = Modifier.align(Alignment.CenterHorizontally))
                TextContent(
                    text = "${points?.rank ?: 0}",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }

        }
    }
}

@Composable
private fun ArticleItem(
    index: Int,
    article: Article,
    onItemClick: () -> Unit
) {
    Column(
        Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .clickable { onItemClick.invoke() }
    ) {
        TextContent(text = "${index + 1}. ${article.title}")
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TimerIcon(modifier = Modifier.padding(start = 10.dp))
            MiniTitle(text = "${RegexUtils().timestamp(article.niceDate)}")
        }
    }
}