package com.mm.hamcompose.ui.page.main.category.structure.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.ToolBarHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.MediumTitle
import com.mm.hamcompose.ui.widget.MultiStateItemView
import com.mm.hamcompose.ui.widget.SNACK_INFO
import com.mm.hamcompose.ui.widget.popupSnackBar

internal const val TIPS_TEXT = "输入作者名称"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StructureListPage(
    parent: ParentBean?,
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: StructureListViewModel = hiltViewModel()
) {
    parent ?: return
    viewModel.setId(parent.id)
    viewModel.start()

    var isShowInput by remember { mutableStateOf(false) }
    val authorName by remember { viewModel.authorName }
    val message by remember { viewModel.message }
    val articles = viewModel.articles.value?.collectAsLazyPagingItems()
    val refreshing by remember { viewModel.isRefreshing }
    val swipeRefreshState = rememberSwipeRefreshState(refreshing)
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)
    val keyboard = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()

    if (message.isNotEmpty()) {
        popupSnackBar(coroutineScope, scaffoldState, SNACK_INFO, message)
        viewModel.message.value = ""
    }


    Column {
        if (isShowInput) {
            InputSearchBar(
                keyWord = authorName,
                onTextChange = {
                    viewModel.authorName.value = it
                    if (it.isEmpty()) {
                        viewModel.refresh("")
                    }
                },
                onSearchClick = {
                    viewModel.searchByAuthor(it)
                },
                navCtrl = navCtrl
            )
        }
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refresh(authorName) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            keyboard?.hide()
                        }
                    )
                }
        ) {
            LazyColumn(
                state = listState,
            ) {
                if (articles != null && articles.itemCount > 0) {
                    itemsIndexed(articles) { index, item ->
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
                                    articles.peek(index)?.collect = false
                                } else {
                                    viewModel.collectArticleById(it)
                                    articles.peek(index)?.collect = true
                                }

                            },
                            onUserClick = { userId ->
                                RouteUtils.navTo(navCtrl, RouteName.SHARER, userId)
                            })
                    }
                } else {
                    items(5) {
                        MultiStateItemView(data = Article(), isLoading = true)
                    }
                }
                isShowInput = listState.firstVisibleItemIndex <= 0
            }
        }
    }
}

/**
 * 搜索框
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputSearchBar(
    keyWord: String,
    onTextChange: (text: String) -> Unit,
    onSearchClick: (key: String) -> Unit,
    navCtrl: NavHostController
) {
    val inputService = LocalTextInputService.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(HamTheme.colors.themeUi)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                null,
                Modifier
                    .clickable(onClick = { navCtrl.back() })
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp)
                    .size(25.dp),
                tint = HamTheme.colors.mainColor
            )
            BasicTextField(
                value = keyWord,
                onValueChange = {
                    onTextChange.invoke(it)
                },
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { inputService?.hideSoftwareKeyboard() },
                cursorBrush = SolidColor(HamTheme.colors.textSecondary)
            )

            if (keyWord.trim().isNotEmpty()) {
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
}
