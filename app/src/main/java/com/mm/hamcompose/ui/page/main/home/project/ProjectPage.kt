package com.mm.hamcompose.ui.page.main.home.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ListTitleHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*
import com.mm.hamcompose.util.RegexUtils

private const val Newest = "最新"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProjectPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: ProjectViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int) -> Unit,
) {

    viewModel.start()

    val refreshing by remember { viewModel.isRefreshing }
    val currentListPosition by remember { viewModel.currentListIndex }
    val currentRowPosition by remember { viewModel.currentRowIndex }
    val message by remember { viewModel.message }
    val projects = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val isLoaded = projects?.loadState?.prepend?.endOfPaginationReached ?: false
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val rowListState = rememberLazyListState(currentRowPosition)
    val listState = rememberLazyListState(currentListPosition)

    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }

    Column {
        LabelList(viewModel, rowListState) { data, position ->
            viewModel.saveRowPosition(rowListState.firstVisibleItemIndex)
            viewModel.setupProjectId(data.id)
            viewModel.triggerRefresh()
            viewModel.refresh()
        }
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = {
                viewModel.triggerRefresh()
                viewModel.refresh()
            },
        ) {
            LazyColumn(state = listState) {
                if (isLoaded) {
                    if (projects!!.itemCount > 0) {
                        itemsIndexed(projects) { index, item ->
                            ProjectItem(
                                project = item!!,
                                onClick = {
                                    viewModel.savePosition(listState.firstVisibleItemIndex)
                                    val webData = WebData(item.title, item.link!!)
                                    RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, webData)
                                },
                                onFavouriteClick = { projectId ->
                                    if (item.collect) {
                                        viewModel.uncollectArticleById(projectId)
                                        projects.peek(index)?.collect = false
                                    } else {
                                        viewModel.collectArticleById(projectId)
                                        projects.peek(index)?.collect = true
                                    }
                                }
                            )
                        }
                        onScrollChangeListener(listState.firstVisibleItemIndex)
                    } else {
                        item { EmptyView() }
                    }
                } else {
                    items(5) {
                        ProjectItem(
                            project = Article(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LabelList(
    viewModel: ProjectViewModel,
    listState: LazyListState,
    onClick: (ParentBean, Int) -> Unit
) {

    val category by remember { viewModel.list }
    val tabIndex by remember { viewModel.tabIndex }

    if (category.isNotEmpty()) {

        if (category[0].name != Newest) {
            category.add(0, ParentBean(null, id = -1, name = Newest))
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = ListTitleHeight)
                .pointerInteropFilter { false },//事件拦截,
            state = listState
        ) {
            itemsIndexed(category) { index, item ->
                TextButton(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = {
                        viewModel.setTabIndex(index)
                        onClick(item, index)
                    }
                ) {
                    Text(
                        text = item.name ?: "标签",
                        color = if (index == tabIndex) HamTheme.colors.themeUi else HamTheme.colors.textPrimary
                    )
                }
            }
        }
    }

}


@Composable
private fun ProjectItem(
    project: Article,
    onClick: () -> Unit = {},
    onFavouriteClick: (id: Int) -> Unit = {},
    isLoading: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(5.dp)
            .clickable(enabled = !isLoading) {
                onClick.invoke()
            }
    ) {

        Row {
            NetworkImage(
                url = project.envelopePic!!,
                isLoading = isLoading,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                MainTitle(
                    title = project.title ?: "标题",
                    color = HamTheme.colors.textPrimary,
                    maxLine = 2,
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isLoading
                )
                TextContent(
                    text = project.desc ?: "内容",
                    maxLines = 4,
                    color = HamTheme.colors.textSecondary,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    isLoading = isLoading
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {
                        UserIcon(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            isLoading = isLoading
                        )
                        MiniTitle(
                            text = project.author!!,
                            color = HamTheme.colors.textSecondary,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically),
                            isLoading = isLoading
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {

                        TimerIcon(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            isLoading = isLoading
                        )
                        MiniTitle(
                            text = RegexUtils().timestamp(project.niceDate) ?: "",
                            color = HamTheme.colors.textSecondary,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically),
                            maxLines = 1,
                            isLoading = isLoading,
                        )
                    }

                    FavouriteIcon(
                        isFavourite = project.collect,
                        onClick = {
                            onFavouriteClick.invoke(project.id)
                        },
                        isLoading = isLoading
                    )

                }
            }
        }

    }
}
