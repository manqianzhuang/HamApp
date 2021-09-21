package com.mm.hamcompose.ui.page.main.category.navigation

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

@Composable
fun NaviPage(
    navCtrl: NavHostController,
    viewModel: NaviViewModel = hiltViewModel()
) {
    viewModel.start()
    val naviData by remember { viewModel.list }
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(HamTheme.colors.background),
        state = listState,
        contentPadding = PaddingValues(10.dp)
    ) {
        val data = naviData as List<NaviWrapper>
        itemsIndexed(data) { index, naviBean ->
            NaviItem(naviBean, onSelected = {
                viewModel.savePosition(listState.firstVisibleItemIndex)
                RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, it)
            })
            if (index<=data.size-1) {
                Divider(startIndent = 10.dp, color = HamTheme.colors.divider, thickness = 0.8f.dp)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun NaviItem(wrapper: NaviWrapper, onSelected: (WebData) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = wrapper.name?: "标签", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (!wrapper.articles.isNullOrEmpty()) {
            FlowRow {
                for(item in wrapper.articles!!) {
                    LabelTextButton(
                        text = item.title?:"android",
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
