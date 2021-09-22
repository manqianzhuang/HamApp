package com.mm.hamcompose.ui.page.main.profile.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    viewModel.start()
    val historyList by remember { viewModel.list }
    val isClear by remember { viewModel.isClear }
    val asyncScope = rememberCoroutineScope()

    if (isClear) {
        popupSnackBar(asyncScope, scaffoldState, SNACK_INFO, "历史记录已清空")
        viewModel.isClear.value = false
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HamToolBar(
            title = "历史浏览记录",
            rightText = "清除所有",
            onBack = { navCtrl.back() },
            onRightClick = {
                viewModel.clearAllHistory()
            }
        )
        ListTitle(title = "最近在看", modifier = Modifier.padding(top = 12.dp, bottom = 5.dp))
        if (historyList.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(historyList) { index, item ->
                    TextContent(
                        text = "${index + 1}. ${item.title}",
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .clickable {
                                RouteUtils.navTo(
                                    navCtrl = navCtrl,
                                    destinationName = RouteName.WEB_VIEW,
                                    args = WebData(item.title, item.link)
                                )
                            },
                        maxLines = 2,
                    )
                }

            }
        } else {
            EmptyView(tips = "暂无浏览记录", imageVector = Icons.Default.Info)
        }

    }
}