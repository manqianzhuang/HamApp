package com.mm.hamcompose.ui.page.main.category.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.data.bean.NaviWrapper
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteName

import com.mm.hamcompose.ui.widget.LabelTextButton
import com.mm.hamcompose.ui.widget.ListTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NaviPage(
    navCtrl: NavHostController,
    viewModel: NaviViewModel = hiltViewModel()
) {
    viewModel.start()
    val naviData by remember { viewModel.list }
    val isLoading by remember { viewModel.loading }
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        if (isLoading) {
            items(6) {
                NaviItem(
                    wrapper = NaviWrapper(null, -1, ""),
                    isLoading = isLoading,
                )
            }
        } else {
            naviData.forEachIndexed { index, naviBean ->
                stickyHeader { ListTitle(title = naviBean.name ?: "标题") }
                item {
                    NaviItem(naviBean, onSelected = {
                        viewModel.savePosition(listState.firstVisibleItemIndex)
                        RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, it)
                    })
                    if (index <= naviData.size - 1) {
                        Divider(
                            startIndent = 10.dp,
                            color = HamTheme.colors.divider,
                            thickness = 0.8f.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun NaviItem(
    wrapper: NaviWrapper,
    isLoading: Boolean = false,
    onSelected: (WebData) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        if (isLoading) {
            ListTitle(title = "我是标题")
            FlowRow(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                for (i in 0..7) {
                    LabelTextButton(
                        text =  "android",
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        isLoading = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            if (!wrapper.articles.isNullOrEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    for (item in wrapper.articles!!) {
                        LabelTextButton(
                            text = item.title ?: "android",
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                            onClick = {
                                val webData = WebData(item.title, item.link!!)
                                onSelected(webData)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}
