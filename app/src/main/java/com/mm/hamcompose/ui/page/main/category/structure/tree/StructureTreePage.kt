package com.mm.hamcompose.ui.page.main.category.structure.tree

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.LabelTextButton
import com.mm.hamcompose.ui.widget.ListTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StructurePage(
    navCtrl: NavHostController,
    viewModel: StructureViewModel = hiltViewModel()
) {

    viewModel.start()
    val systemData by remember { viewModel.list }
    val isLoading by remember { viewModel.loading }
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(HamTheme.colors.background),
        state = listState,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {

        if (isLoading) {
            items(5) {
                StructureItem(ParentBean(null), isLoading = true)
            }
        } else {
            systemData.forEachIndexed { position, chapter1 ->
                stickyHeader { ListTitle(title = chapter1.name ?: "标题") }
                item {
                    StructureItem(chapter1, onSelect = { parent->
                        viewModel.savePosition(listState.firstVisibleItemIndex)
                        RouteUtils.navTo(navCtrl, RouteName.STRUCTURE_LIST, parent)
                    })
                    if (position <= systemData.size - 1) {
                        Divider(startIndent = 10.dp, color = HamTheme.colors.divider, thickness = 0.8f.dp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun StructureItem(
    bean: ParentBean,
    isLoading: Boolean = false,
    onSelect: (parent: ParentBean) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
    ) {
        if (isLoading) {
            ListTitle(title = "我都标题", isLoading = true)
            FlowRow(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                for (i in 0..7) {
                    LabelTextButton(
                        text = "android",
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        isLoading = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            if (!bean.children.isNullOrEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    for (item in bean.children!!) {
                        LabelTextButton(
                            text = item.name ?: "android",
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                            onClick = {
                                onSelect(item)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}
