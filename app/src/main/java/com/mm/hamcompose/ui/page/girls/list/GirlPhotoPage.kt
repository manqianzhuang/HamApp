package com.mm.hamcompose.ui.page.girls.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.WelfareData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.themeColors
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.HamToolBar
import com.mm.hamcompose.ui.widget.TextContent

private const val TAG = "Ham"

/**
 * 看妹纸 页面
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GirlPhotoPage(
    navCtrl: NavHostController,
    viewModel: GirlPhotoViewModel = hiltViewModel()
) {

    viewModel.start()
    val girls by remember { viewModel.photoData }
    val gridState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HamTheme.colors.background)
    ) {
        HamToolBar(title = "福利", onBack = { navCtrl.back() })
        if (girls.isNotEmpty()) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp),
                state = gridState,
            ) {
                itemsIndexed(girls) { index, welfare ->
                    PhotoItem(
                        welfare = welfare,
                        onClick = {
                            RouteUtils.navTo(navCtrl, RouteName.GIRL_INFO, girls[index])
                        })
                }
                Log.d(TAG, "GirlPhotoPage: ${gridState.firstVisibleItemIndex}")
                if (gridState.firstVisibleItemIndex % viewModel.pageSize > 15) {
                    viewModel.loadMore()
                }
            }
        }

    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun PhotoItem(welfare: WelfareData, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {

        Image(
            painter = rememberImagePainter(
                data = welfare.url,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.no_banner)
                },
            ),
            contentDescription = "空图片",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick.invoke() }
        )
    }
}


@Preview
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateTest() {
    /** test code start */
    var visible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {

        TextContent(
            text = "点击CrossFade",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .clickable { visible = !visible }
        )

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 20.dp)
                    .background(color = themeColors[0])
            )
        }
    }
    /** test code end */
}
