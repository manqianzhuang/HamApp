package com.mm.hamcompose.ui.page.main.category.structure.tree

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

@Composable
fun StructurePage(
    navCtrl: NavHostController,
    viewModel: StructureViewModel = hiltViewModel()
) {

    viewModel.start()
    val systemData by remember { viewModel.list }
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
        val data = systemData as List<ParentBean>
        itemsIndexed(data) { position, systemBean ->
            StructureItem(systemBean, onSelect = { parent->
                viewModel.savePosition(listState.firstVisibleItemIndex)
                RouteUtils.navTo(navCtrl, RouteName.STRUCTURE_LIST, parent)//parent.id)
            })
            if (position <= data.size - 1) {
                Divider(startIndent = 10.dp, color = HamTheme.colors.divider, thickness = 0.8f.dp)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}

@Composable
fun StructureItem(bean: ParentBean, onSelect: (parent: ParentBean) -> Unit,) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = bean.name ?: "标签", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (!bean.children.isNullOrEmpty()) {
            FlowRow {
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
