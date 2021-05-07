package com.mm.hamcompose.ui.page.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.bean.NaviWrapper
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.widget.LabelText

@Composable
fun NaviPage(
    onSelected: (String, Any?) -> Unit,
    viewModel: NaviViewModel = viewModel(NaviViewModel::class.java)
) {
    LogUtils.e("刷新导航")

    viewModel.start()
    val naviData = viewModel.list.observeAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(HamTheme.colors.background),
        contentPadding = PaddingValues(10.dp)
    ) {
        val data = naviData.value as List<NaviWrapper>
        itemsIndexed(data) { index, naviBean ->
            NaviItem(naviBean, onSelected = onSelected)
            if (index<=data.size-1) {
                Divider(startIndent = 10.dp, color = HamTheme.colors.divider, thickness = 0.8f.dp)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun NaviItem(wrapper: NaviWrapper, onSelected: (String, Any?) -> Unit,) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = wrapper.name?: "标签", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (!wrapper.articles.isNullOrEmpty()) {
            FlowRow {
                for(item in wrapper.articles!!) {
                    LabelText(
                        text = item.title?:"android",
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        onClick = {
                            val webData = WebData(item.title, item.link!!)
                            onSelected(HamRouter.webView, webData)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
