package com.mm.hamcompose.ui.page.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.statusBarsPadding
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.Hotkey
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.index.IndexItem
import com.mm.hamcompose.ui.widget.LabelText
import com.mm.hamcompose.ui.widget.MediumTitle
import com.mm.hamcompose.ui.widget.TextContent

@Composable
fun SearchPage(
    actions: RouteActions,
    viewModel: SearchViewModel = viewModel(SearchViewModel::class.java)
) {

    viewModel.start()

    Box {
        var keyWord by remember { mutableStateOf("") }
        val queries = viewModel.searches.value?.collectAsLazyPagingItems()
        val hotkeys = viewModel.hotkeys.observeAsState()
        val history = viewModel.history.observeAsState()

        LazyColumn(Modifier.padding(top = 48.dp)) {
            item {
                // part1. 搜索热词
                if (hotkeys.value!!.isNotEmpty()) {
                    MediumTitle(
                        title = "搜索热词",
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                    )
                    Box {
                        HotkeyItem(
                            hotkeys = hotkeys.value!!,
                            viewModel = viewModel,
                            onSelected = { text ->
                                keyWord = text
                            })
                    }
                }
                //part2. 历史记录
                if (history.value != null && history.value!!.isNotEmpty()) {
                    Row(Modifier.padding(10.dp)) {
                        MediumTitle(title = "搜索历史")
                        Box(modifier = Modifier.weight(1f))
                        TextContent(
                            title = "清空",
                            modifier = Modifier.clickable {
                                viewModel.deleteAll()
                            }
                        )
                    }
                    Column {
                        history.value!!.forEach { item ->
                            HistoryItem(
                                data = item,
                                viewModel = viewModel,
                                onSelected = {
                                    keyWord = it
                                }
                            )
                        }
                    }
                }
            }

            // part3. 搜索列表
            if (queries != null) {
                item {
                    MediumTitle(title = "搜索内容", modifier = Modifier.padding(10.dp))
                }
                items(queries) { item ->
                    IndexItem(data = item!!, onSelected = { routeName, data ->
                        actions.selected(routeName, data)
                    })
                }
            }
        }

        LogUtils.e("重新渲染")
        SearchHead(
            keyWord = keyWord,
            onTextChange = {
                keyWord = it
            },
            onSearchClick = {
                /**
                 * 重点：
                 *  必须赋空字符串值，因为keyWord驱动state刷新页面，
                 *  如果不改变keyWord状态，那么paging框架将不执行
                 */
                keyWord = ""
                if (it.trim().isNotEmpty()) {
                    keyWord = it
                    viewModel.insertKey(it)
                    viewModel.search(it)
                }
            },
            actions = actions
        )
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
    actions: RouteActions
) {

    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(HamApp.CONTEXT.resources.getColor(R.color.teal_200)))
            .statusBarsPadding()
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
                    .clickable(onClick = actions.backPress)
                    .align(Alignment.CenterVertically)
                    .size(20.dp)
                    .padding(end = 10.dp),
                tint = HamTheme.colors.onBadge
            )
            BasicTextField(
                value = keyWord,
                onValueChange = { onTextChange(it) },
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
            )
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
            LabelText(
                text = it.name ?: "",
                isSelect = false,
                modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                onClick = {
                    LogUtils.e("点击热词=${it.name!!}")
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
            title = data.name ?: "",
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
                .clickable {
                    onSelected(data.name!!)
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
