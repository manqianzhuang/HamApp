package com.mm.hamcompose.ui.page.project.hot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.project.category.ProjectItem
import com.mm.hamcompose.ui.widget.LabelText
import com.mm.hamcompose.ui.widget.TextContent

@Composable
fun ProjectPage(
    viewModel: ProjectHotViewModel = viewModel(ProjectHotViewModel::class.java),
    actions: RouteActions
) {
    Box {
        val refreshing = viewModel.isRefreshing.observeAsState()
        val projects = viewModel.pagingData.value?.collectAsLazyPagingItems()
        val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                LogUtils.e("刷新数据")
                viewModel.isRefreshing.value = true
                viewModel.pagingData.value = null
                viewModel.refresh()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        ) {
            if (projects != null) {
                LazyColumn {
                    items(projects) { item ->
                        ProjectItem(item!!, actions)
                    }
                }
            }
        }
        LabelList(viewModel, actions.selected)

        viewModel.start()
    }
}

@Composable
fun LabelList(viewModel: ProjectHotViewModel, onClick: (String, ParentBean) -> Unit) {

    val category = viewModel.list.observeAsState()
    val tabIndex = viewModel.tabIndex.observeAsState()

    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .background(HamTheme.colors.textFieldBackground)
            .padding(horizontal = 10.dp)
            .pointerInteropFilter { false }//事件拦截
    ) {

        val expandable = viewModel.labelExpand.observeAsState()

        if (expandable.value!!) {
            FlowRow(modifier = Modifier.weight(1f).padding(top = 5.dp)
            ) {
                category.value!!.forEachIndexed { index, item ->
                    LabelText(
                        text = item.name ?: "标签",
                        modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                        isSelect = index == tabIndex.value,
                        onClick = {
                            viewModel.setLabelExpand(!expandable.value!!)
                            viewModel.setTabIndex(index)
                            onClick(HamRouter.projectDetail, item)
                        })
                }
            }
        } else {

            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
            ) {
                val selects = viewModel.selectLabels
                if (selects.isNotEmpty()) {
                    selects.forEach { position->
                        LabelText(
                            text = category.value!![position].name ?: "标签",
                            modifier = Modifier.width(80.dp).padding(end = 5.dp),
                            isSelect = true,
                            onClick = {
                                onClick(HamRouter.projectDetail, category.value!![position])
                            })
                    }
                }
                if (selects.size<4) {
                    category.value!!.forEachIndexed { index, item ->
                        if (index< 4-selects.size) {
                            LabelText(
                                text = item.name ?: "标签",
                                modifier = Modifier.width(80.dp).padding(end = 5.dp),
                                isSelect = false,
                                onClick = {
                                    onClick(HamRouter.projectDetail, item)
                                })
                        }
                    }
                }

//            val lazyRowState = rememberLazyListState()
//            val coroutineScope = rememberCoroutineScope()
//             coroutineScope.launch {
//                        lazyRowState.scrollToItem(tabIndex.value!!)
//            }
            }
        }

        TextContent(
            title = if (!expandable.value!!) "展开" else "收起",
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                   viewModel.setLabelExpand(!expandable.value!!)
                }
            ,
            color = HamTheme.colors.themeUi
        )

    }
}