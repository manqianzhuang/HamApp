package com.mm.hamcompose.ui.page.main.home.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.H4
import com.mm.hamcompose.theme.H6
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ListTitleHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*
import com.mm.hamcompose.util.RegexUtils


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProjectPage(
    navCtrl: NavHostController,
    viewModel: ProjectViewModel = hiltViewModel(),
    onScrollChangeListener: (position: Int)-> Unit,
) {

    viewModel.start()

    val refreshing by remember { viewModel.isRefreshing }
    val currentListPosition by remember { viewModel.currentListIndex }
    val currentRowPosition by remember { viewModel.currentRowIndex }
    val projects = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val rowListState = rememberLazyListState(currentRowPosition)
    val listState = rememberLazyListState(currentListPosition)

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
            if (projects != null) {
                LazyColumn(
                    state = listState
                ) {
                    itemsIndexed(projects) { position, item ->
                        ProjectItem(item!!) {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            val webData = WebData(item.title, item.link!!)
                            RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, webData)
                        }
                    }
                }
                onScrollChangeListener(listState.firstVisibleItemIndex)
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

        if (category[0].name != "热门") {
            category.add(0, ParentBean(null, id = -1, name = "热门"))
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
private fun ProjectItem(project: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(5.dp)
            .clickable {
                onClick.invoke()
            }
    ) {

        Row {
            Box(modifier = Modifier.width(100.dp)) {
                val painter = rememberCoilPainter(
                    request = project.envelopePic!!,
                    fadeIn = true,
                    previewPlaceholder = R.drawable.no_banner
                )
                Image(
                    painter = painter,
                    contentDescription = "Project Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                if (painter.loadState == ImageLoadState.Empty) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = HamTheme.colors.themeUi
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                MainTitle(
                    title = project.title ?: "标题",
                    color = HamTheme.colors.textPrimary,
                    maxLine = 2
                )
                TextContent(
                    text = project.desc ?: "内容",
                    maxLines = 5,
                    color = HamTheme.colors.textSecondary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                ) {
                    Row(modifier = Modifier.align(Alignment.CenterStart)) {
                        UserIcon(modifier = Modifier.align(Alignment.CenterVertically))
                        MiniTitle(
                            text = project.author!!,
                            color = HamTheme.colors.textSecondary,
                            modifier = Modifier.padding(end = 5.dp).align(Alignment.CenterVertically)
                        )
                    }

                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        TimerIcon(modifier = Modifier.align(Alignment.CenterVertically))
                        MiniTitle(
                            text = RegexUtils().timestamp(project.niceDate) ?: "2020-02",
                            color = HamTheme.colors.textSecondary,
                            modifier = Modifier.width(80.dp).align(Alignment.CenterVertically),
                            maxLines = 1,
                        )
                    }

                }
            }
        }

    }
}
