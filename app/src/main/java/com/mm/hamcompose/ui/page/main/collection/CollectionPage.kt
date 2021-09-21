package com.mm.hamcompose.ui.page.main.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.BottomNavBarHeight
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollectionPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: CollectionViewModel = hiltViewModel()
) {

    viewModel.start()

    var dialogAlert by remember { mutableStateOf(false) }
    val titles by remember { viewModel.titles }
    val webUrls by remember { viewModel.webUrlList }
    var currentLabelIndex by remember { mutableStateOf(0) }
    val articles = viewModel.pagingArticleCollect.value?.collectAsLazyPagingItems()
    val listIndex by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(listIndex)
    val deleteTitle by remember { viewModel.uncollectArticleTitle }
    val scope = rememberCoroutineScope()

    if (deleteTitle != null) {
        popSnack(scope, scaffoldState, SNACK_SUCCESS, "已删除") {
            viewModel.uncollectArticleTitle.value = null
        }
    }

    if (dialogAlert) {
        SelectAlertDialog(
            title = "提示",
            content = "请选择以下操作",
            primaryButtonText = "删除",
            secondButtonText = "编辑",
            onPrimaryButtonClick = {
                viewModel.deleteWebsite(webUrls!![currentLabelIndex].id)
            },
            onSecondButtonClick = {
                RouteUtils.navTo(navCtrl, RouteName.EDIT_WEBSITE, webUrls!![currentLabelIndex])
            },
            onDismiss = { dialogAlert = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = BottomNavBarHeight)
    ) {
        HamToolBar(
            title = "我的收藏",
            onRightClick = {
                RouteUtils.navTo(navCtrl, RouteName.EDIT_WEBSITE)
            },
            imageVector = Icons.Default.Edit)

        if (webUrls.isNullOrEmpty() && articles == null) {
            EmptyView(tips = "啥都没有~", imageVector = Icons.Default.Create) {
                RouteUtils.navTo(navCtrl, RouteName.EDIT_WEBSITE)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(top = 10.dp)
            ) {
                stickyHeader {
                    ListTitle(title = titles[1].text)
                }
                item {
                    FlowRow(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        webUrls?.forEachIndexed { index, website ->
                            LabelTextButton(
                                text = website.name ?: "标签",
                                modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
                                onClick = {
                                    viewModel.resetListIndex()
                                    RouteUtils.navTo(
                                        navCtrl,
                                        RouteName.WEB_VIEW,
                                        WebData(website.name, website.link!!)
                                    )
                                },
                                onLongClick = {
                                    currentLabelIndex = index
                                    dialogAlert = true
                                }
                            )
                        }
                    }
                }
                stickyHeader {
                    ListTitle(title = titles[0].text)
                }
                items(articles!!) { collectItem ->
                    CollectListItemView(
                        collectItem!!,
                        onClick = {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            RouteUtils.navTo(
                                navCtrl,
                                RouteName.WEB_VIEW,
                                WebData(collectItem.title, collectItem.link)
                            )
                        }, onDeleteClick = {
                            with(collectItem) {
                                viewModel.uncollectArticle(id, originId, title)
                            }
                        })
                }
            }

        }
    }
}

