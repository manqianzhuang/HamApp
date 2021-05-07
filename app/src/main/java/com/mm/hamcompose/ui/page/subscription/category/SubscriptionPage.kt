package com.mm.hamcompose.ui.page.subscription.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.Teal200
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.HamRouter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubscriptionPage(
    viewModel: SubscriptionViewModel = viewModel(SubscriptionViewModel::class.java),
    onSelected: (String, ParentBean) -> Unit
) {
    viewModel.start()
    var publicNoData = viewModel.list.observeAsState()

    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(HamTheme.colors.background)
            .padding(10.dp)
    ) {
        if (publicNoData.value!=null) {
            itemsIndexed(publicNoData.value!!) { index, item ->
                Box(Modifier.padding(vertical = 5.dp)) {
                    when (index % 4) {
                        0 -> LeftTopItem(item) {
                            onSelected(HamRouter.subscriptionDetail, it)
                        }
                        1 -> RightTopItem(item) {
                            onSelected(HamRouter.subscriptionDetail, it)
                        }
                        2 -> LeftBottomItem(item) {
                            onSelected(HamRouter.subscriptionDetail, it)
                        }
                        3 -> RightBottomItem(item) {
                            onSelected(HamRouter.subscriptionDetail, it)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SpecialText(
    parent: ParentBean,
    textColor: Color,
    bgColor: Color,
    shape: RoundedCornerShape,
    onClick: (ParentBean)-> Unit
) {
    Text(
        text = parent.name!!,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = textColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = bgColor,
                shape = shape
            )
            .clickable {
                onClick(parent)
            }
            .padding(top = 40.dp),
        fontWeight = FontWeight.W500
    )
}

@Composable
fun LeftTopItem(parent: ParentBean, click: (ParentBean)-> Unit) {
    SpecialText(
        parent = parent,
        textColor = white1,
        bgColor = Teal200,
        shape = RoundedCornerShape(topStart = 50.dp)
    ) {
        click(it)
    }
}

@Composable
fun LeftBottomItem(parent: ParentBean, click: (ParentBean)-> Unit) {
    SpecialText(
        parent = parent,
        textColor = HamTheme.colors.textSecondary,
        bgColor = white1,
        shape = RoundedCornerShape(topStart = 50.dp),
    ) {
        click(it)
    }
}

@Composable
fun RightTopItem(parent: ParentBean, click: (ParentBean)-> Unit) {
    SpecialText(
        parent = parent,
        textColor = HamTheme.colors.textSecondary,
        bgColor = white1,
        shape = RoundedCornerShape(bottomEnd = 50.dp),
    ) {
        click(it)
    }
}

@Composable
fun RightBottomItem(parent: ParentBean, click: (ParentBean)-> Unit) {
    SpecialText(
        parent = parent,
        textColor = white1,
        bgColor = Teal200,
        shape = RoundedCornerShape(bottomEnd = 50.dp),
    ) {
        click(it)
    }
}

fun getFirstCharFromName(text: String): String {
    return text.trim().substring(0, 1)
}


