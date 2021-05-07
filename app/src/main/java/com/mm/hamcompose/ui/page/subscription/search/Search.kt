package com.mm.hamcompose.ui.page.subscription.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.subscription.author.SubscriptionItem

@Composable
fun SubscriptionSearch(
    parent: ParentBean,
    actions: RouteActions,
    viewModel: SubscriptionSearchViewModel = viewModel(SubscriptionSearchViewModel::class.java),
) {
    viewModel.setPublicId(parent.id)
    //viewModel.start()
    val searchResult = viewModel.searchResult.value?.collectAsLazyPagingItems()
    val key = remember { mutableStateOf("") }

    Box {

        val refreshing = viewModel.isRefreshing.observeAsState()
        val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                LogUtils.e("刷新数据")
                viewModel.refreshSearch(key.value)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (searchResult != null) {
                LazyColumn {
                    items(searchResult) { item ->
                        SubscriptionItem(item!!, actions)
                    }
                }
            }
        }

        SearchHead(
            key = key.value,
            viewModel = viewModel,
            onKeyChange = { word -> key.value = word },
            backPress = actions.backPress
        )
    }
}

@Composable
fun SearchHead(
    key: String,
    viewModel: SubscriptionSearchViewModel,
    onKeyChange: (word: String) -> Unit,
    backPress: () -> Unit
) {

    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(HamApp.CONTEXT.resources.getColor(R.color.teal_200)))
            .statusBarsPadding()
    ) {
        Row(Modifier.align(Alignment.Center)) {
            Icon(
                painterResource(R.drawable.icon_back_white),
                null,
                Modifier
                    .clickable(onClick = backPress)
                    .align(Alignment.CenterVertically)
                    .size(30.dp)
                    .padding(horizontal = 10.dp),
                tint = HamTheme.colors.onBadge
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
            )
        }

    }
}
