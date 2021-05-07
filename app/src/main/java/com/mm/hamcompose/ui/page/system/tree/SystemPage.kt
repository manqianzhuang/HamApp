package com.mm.hamcompose.ui.page.system.tree

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
import com.google.accompanist.flowlayout.FlowRow
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.widget.LabelText

@Composable
fun SystemPage(
    onSelected: (routeName: String, parent: ParentBean) -> Unit,
    viewModel: SystemViewModel = viewModel(SystemViewModel::class.java)
) {

    viewModel.start()
    val systemData = viewModel.list.observeAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(HamTheme.colors.background),
        contentPadding = PaddingValues(10.dp)
    ) {
        val data = systemData.value as List<ParentBean>
        itemsIndexed(data) { index, systemBean ->
            SystemItem(systemBean, onSelect = onSelected)
            if (index <= data.size - 1) {
                Divider(startIndent = 10.dp, color = HamTheme.colors.divider, thickness = 0.8f.dp)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SystemItem(bean: ParentBean, onSelect: (routeName: String, parent: ParentBean) -> Unit,) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = bean.name ?: "标签", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (!bean.children.isNullOrEmpty()) {
            FlowRow {
                for (item in bean.children!!) {
                    LabelText(
                        text = item.name ?: "android",
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        onClick = {
                            onSelect(HamRouter.systemCategory, item)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}
