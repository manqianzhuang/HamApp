package com.mm.hamcompose.ui.page.main.category.pubaccount.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ToolBarHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.EmptyView
import com.mm.hamcompose.ui.widget.SimpleListItemView

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PublicAccountSearch(
    parent: ParentBean?,
    navCtrl: NavHostController,
    viewModel: PublicAccountSearchViewModel = hiltViewModel(),
) {
    parent ?: return
    viewModel.setPublicId(parent.id)
    viewModel.start()
    val searchResult = viewModel.searchResult.value?.collectAsLazyPagingItems()
    val searchContent by remember { viewModel.searchContent }
    val refreshing by remember { viewModel.isRefreshing }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val keyboardCtrl = LocalSoftwareKeyboardController.current
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    Column {
        SearchHead(
            key = searchContent,
            viewModel = viewModel,
            onKeyChange = {
                viewModel.searchContent.value = it
            },
            backPress = {
                navCtrl.back()
            },
            softwareKeyboardController = keyboardCtrl
        )

        if (searchContent.isNotEmpty()) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.refreshSearch(searchContent)
                },
                modifier = Modifier.fillMaxSize()
            ) {
                if (searchResult != null) {
                    LazyColumn(state = listState) {
                        items(searchResult) { item ->
                            SimpleListItemView(
                                data = item!!,
                                onClick = {
                                    viewModel.saveDataToHistory(item)
                                    viewModel.savePosition(listState.firstVisibleItemIndex)
                                    RouteUtils.navTo(
                                        navCtrl, RouteName.WEB_VIEW, WebData(item.title, item.link!!))
                                },
                                onCollectClick = {
                                    if (item.collect) {
                                        viewModel.uncollectArticleById(it)
                                        item.collect = false
                                    } else {
                                        viewModel.collectArticleById(it)
                                        item.collect = true
                                    }

                                })
                        }
                    }
                }
            }
        } else {
            EmptyView("输入关键字搜索") {
                keyboardCtrl?.hide()
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchHead(
    key: String,
    viewModel: PublicAccountSearchViewModel,
    onKeyChange: (word: String) -> Unit,
    backPress: () -> Unit,
    softwareKeyboardController: SoftwareKeyboardController?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(HamTheme.colors.themeUi)
    ) {
        Row(Modifier.align(Alignment.Center)) {
            Icon(
                Icons.Default.ArrowBack,
                null,
                Modifier
                    .clickable(onClick = backPress)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp),
                tint = HamTheme.colors.mainColor
            )
            BasicTextField(
                value = key,
                onValueChange = {
                    onKeyChange(it)
                    if (it.trim().isNotEmpty()) {
                        viewModel.search(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(28.dp)
                    .padding(end = 10.dp)
                    .background(
                        color = HamTheme.colors.mainColor,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .padding(start = 10.dp, top = 4.dp)
                    .align(Alignment.CenterVertically),
                maxLines = 1,
                singleLine = true,
                keyboardActions = KeyboardActions {
                    softwareKeyboardController?.hide()
                },
                textStyle = TextStyle(
                    color = HamTheme.colors.textSecondary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }

    }
}
