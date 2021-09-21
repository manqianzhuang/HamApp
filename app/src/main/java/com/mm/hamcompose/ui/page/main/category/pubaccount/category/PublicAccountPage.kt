package com.mm.hamcompose.ui.page.main.category.pubaccount.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mm.hamcompose.data.bean.ParentBean
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PublicAccountPage(
    navCtrl: NavHostController,
    viewModel: PublicAccountViewModel = hiltViewModel(),
) {
    viewModel.start()
    val publicNoData by remember { viewModel.list }
    val currentPosition by remember { viewModel.currentListIndex }
    val listState = rememberLazyListState(currentPosition)

    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier
            .background(HamTheme.colors.background)
            .wrapContentHeight()
            .padding(10.dp),
        state = listState
    ) {
        itemsIndexed(publicNoData) { index, item ->
            Box(Modifier.padding(vertical = 5.dp)) {
                when (index % 4) {
                    0 -> PublicAccountItem(
                        parent = item,
                        click = {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            navToPublicAccountDetail(navCtrl, it)
                        },
                        isPrimary = true,
                        roundedCorner = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                    )
                    1 -> PublicAccountItem(
                        parent = item,
                        click = {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            navToPublicAccountDetail(navCtrl, it)

                        },
                        isPrimary = false,
                        roundedCorner = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                    )
                    2 -> PublicAccountItem(
                        parent = item,
                        click = {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            navToPublicAccountDetail(navCtrl, it)

                        },
                        isPrimary = false,
                        roundedCorner = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                    )
                    3 -> PublicAccountItem(
                        parent = item,
                        click = {
                            viewModel.savePosition(listState.firstVisibleItemIndex)
                            navToPublicAccountDetail(navCtrl, it)

                        },
                        isPrimary = true,
                        roundedCorner = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                    )
                }
            }

        }

    }
}

private fun navToPublicAccountDetail(navCtrl: NavHostController, parent: ParentBean) {
    RouteUtils.navTo(navCtrl, RouteName.PUB_ACCOUNT_DETAIL, parent)
}

@Composable
fun SpecialText(
    parent: ParentBean,
    textColor: Color,
    bgColor: Color,
    shape: RoundedCornerShape,
    onClick: (ParentBean) -> Unit
) {
    Text(
        text = parent.name!!,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = textColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = bgColor,
                shape = shape
            )
            .padding(top = 30.dp)
            .clickable {
                onClick(parent)
            },
        fontWeight = FontWeight.W500,
    )
}

@Composable
private fun PublicAccountItem(
    parent: ParentBean,
    click: (ParentBean) -> Unit,
    isPrimary: Boolean = true,
    roundedCorner: RoundedCornerShape = RoundedCornerShape(5.dp)
) {
    SpecialText(
        parent = parent,
        textColor = if (isPrimary) white1 else HamTheme.colors.textSecondary,
        bgColor = if (isPrimary) HamTheme.colors.themeUi else white1,
        shape = roundedCorner
    ) {
        click(it)
    }
}


