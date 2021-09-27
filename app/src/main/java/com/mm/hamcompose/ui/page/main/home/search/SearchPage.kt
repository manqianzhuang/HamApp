package com.mm.hamcompose.ui.page.main.home.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.Hotkey
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ToolBarHeight
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SearchViewModel = hiltViewModel()
) {

    viewModel.start()

    val queries = viewModel.searches.value?.collectAsLazyPagingItems()
    val hotkeys by remember { viewModel.hotkeys }
    val history by remember { viewModel.history }
    val searchText by remember { viewModel.searchContent }
    var currentPosition by remember { viewModel.currentListIndex }
    val message by remember { viewModel.message }
    val keyboardCtrl = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState(currentPosition)

    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }


    Column(Modifier.fillMaxSize()) {

        SearchHead(
            keyWord = searchText,
            onTextChange = {
                viewModel.searchContent.value = it
            },
            onSearchClick = {
                if (it.trim().isNotEmpty()) {
                    currentPosition = -1
                    viewModel.insertKey(it)
                    viewModel.search(it)
                }
                keyboardCtrl?.hide()
            },
            navController = navCtrl
        )

        LazyColumn(state = listState) {
            // part1. 搜索热词
            stickyHeader {
                ListTitle(title = "搜索热词")
            }
            item {
                if (hotkeys.isNotEmpty()) {
                    Box {
                        HotkeyItem(
                            hotkeys = hotkeys,
                            viewModel = viewModel,
                            onSelected = { text ->
                                viewModel.searchContent.value = text
                            })
                    }
                }
            }
            //part2. 历史记录
            if (history.isNotEmpty()) {
                stickyHeader {
                    ListTitle(
                        title = "搜索历史",
                        subTitle = "清空",
                        onSubtitleClick = {
                            viewModel.deleteAll()
                        })
                }
                history.forEach { item ->
                    item {
                        HistoryItem(
                            data = item,
                            viewModel = viewModel,
                            onSelected = {
                                viewModel.searchContent.value = it
                            }
                        )
                    }
                }
            }
            // part3. 搜索列表
            stickyHeader {
                ListTitle(title = "搜索结果")
            }
            if (queries != null) {
                itemsIndexed(queries) { index, item ->
                    MultiStateItemView(
                        data = item!!,
                        onSelected = { data ->
                            viewModel.saveDataToHistory(item)
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, data)
                        },
                        onCollectClick = {
                            if (item.collect) {
                                viewModel.uncollectArticleById(it)
                                queries.peek(index)?.collect = false
                            } else {
                                viewModel.collectArticleById(it)
                                queries.peek(index)?.collect = true
                            }
                        },
                        onUserClick = { userId ->
                            RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                        })
                }
            }
        }
    }
}

/**
 * 搜索框
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchHead(
    keyWord: String,
    onTextChange: (text: String) -> Unit,
    onSearchClick: (key: String) -> Unit,
    navController: NavHostController
) {

    Box(
        Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(HamTheme.colors.themeUi)
    ) {
        Row(
            Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.icon_back_white),
                null,
                Modifier
                    .clickable(onClick = {
                        navController.popBackStack()
                    })
                    .align(Alignment.CenterVertically)
                    .size(20.dp)
                    .padding(end = 10.dp),
                tint = HamTheme.colors.mainColor
            )
            BasicTextField(
                value = keyWord,
                onValueChange = { onTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(28.dp)
                    .background(
                        color = HamTheme.colors.mainColor,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .padding(start = 10.dp, top = 4.dp)
                    .align(Alignment.CenterVertically),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = HamTheme.colors.textSecondary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchClick(keyWord) }),
            )
            MediumTitle(
                title = "搜索",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp)
                    .combinedClickable(onClick = { onSearchClick(keyWord) }),
                color = HamTheme.colors.mainColor,
            )
        }
    }
}

/**
 * 搜索热词的item
 */
@Composable
fun HotkeyItem(
    hotkeys: MutableList<Hotkey>,
    viewModel: SearchViewModel,
    onSelected: (key: String) -> Unit
) {
    FlowRow(Modifier.padding(10.dp)) {
        hotkeys.forEach {
            LabelTextButton(
                text = it.name ?: "",
                isSelect = false,
                modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                onClick = {
                    viewModel.savePosition(0)
                    onSelected(it.name!!)
                    viewModel.search(it.name!!)
                }
            )
        }
    }
}

/**
 * 历史记录的item
 */
@Composable
fun HistoryItem(data: Hotkey, viewModel: SearchViewModel, onSelected: (key: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = "history",
            modifier = Modifier.size(20.dp),
            tint = HamTheme.colors.textSecondary
        )
        TextContent(
            text = data.name ?: "",
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
                .clickable {
                    onSelected(data.name!!)
                    viewModel.savePosition(0)
                    viewModel.search(data.name!!)
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "delete",
            tint = HamTheme.colors.textSecondary,
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    viewModel.deleteKey(data)
                })
    }
}
