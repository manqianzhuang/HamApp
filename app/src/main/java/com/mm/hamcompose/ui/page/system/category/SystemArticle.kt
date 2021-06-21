package com.mm.hamcompose.ui.page.system.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.index.IndexItem
import com.mm.hamcompose.ui.widget.HamTopBar
import com.mm.hamcompose.ui.widget.MediumTitle

const val EMPTY_TIPS = "输入作者名称"

@Composable
fun SystemArticlePage(
    parent: ParentBean,
    actions: RouteActions,
    viewModel: SystemArticleViewModel = viewModel(SystemArticleViewModel::class.java)
) {
    viewModel.setId(parent.id)

    Box(Modifier.background(HamTheme.colors.background)) {
        var isShowInput by remember { mutableStateOf(false) }
        var author by remember { mutableStateOf(EMPTY_TIPS) }
        val articles = viewModel.articles.value?.collectAsLazyPagingItems()
        val refreshing by viewModel.isRefreshing.observeAsState()
        val swipeRefreshState = rememberSwipeRefreshState(refreshing!!)

        viewModel.initArticles()

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.refresh(author)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        ) {
            Column {
                if (isShowInput) {
                    SearchBar(
                        keyWord = author,
                        onTextChange = {
                            author = it
                            if (it.isEmpty() || it==EMPTY_TIPS) {
                                viewModel.refresh("")
                            }
                        },
                        onSearchClick = {
                            viewModel.searchByAuthor(it)
                        }
                    )
                }

                if (articles != null) {
                    val lazyState = rememberLazyListState()
                    //val scope = rememberCoroutineScope()
                    LazyColumn(state = lazyState) {
                        items(articles) { item ->
                            IndexItem(item!!, onSelected = { routeName, data ->
                                actions.selected(routeName, data)
                            })
                        }
                    }

                    isShowInput = lazyState.firstVisibleItemIndex <= 0
                    LogUtils.e("偏移量 ${lazyState.firstVisibleItemScrollOffset}")
                    LogUtils.e("偏移值 ${lazyState.firstVisibleItemIndex}")
                }
            }
        }

        LogUtils.e("刷新视图")

        HamTopBar(
            title = parent.name ?: "体系详情",
            onBack = {
                actions.backPress()
            },
            //onSearch = if (isShowInput) null else { { isShowInput = !isShowInput } }
        )
    }
}

/**
 * 搜索框
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchBar(
    keyWord: String,
    onTextChange: (text: String) -> Unit,
    onSearchClick: (key: String) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(HamApp.CONTEXT.resources.getColor(com.mm.hamcompose.R.color.teal_200)))
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {
            BasicTextField(
                value = keyWord,
                onValueChange = {
                    onTextChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(28.dp)
                    .background(
                        color = HamTheme.colors.onBadge,
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
                cursorBrush = SolidColor(HamTheme.colors.textSecondary)
            )
            if (keyWord.trim().isNotEmpty()) {
                MediumTitle(
                    title = "搜索",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                        .combinedClickable(onClick = { onSearchClick(keyWord) }),
                    color = HamTheme.colors.onBadge,
                )
            }
        }
    }
}
