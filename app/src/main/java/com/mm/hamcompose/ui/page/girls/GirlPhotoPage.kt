package com.mm.hamcompose.ui.page.girls

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.HamToolBar

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
//    val girls = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val girls by viewModel.photoData.observeAsState()
    val gridState = rememberLazyListState()
    var lastIndex by remember { mutableStateOf(-1) }

    Column {
        HamToolBar(title = "福利", onBack = { navCtrl.back() })
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            state = gridState,
            modifier = Modifier.background(HamTheme.colors.background)
        ) {
            items(girls!!) { item->
                PhotoItem(item.url)
            }
            val current = gridState.firstVisibleItemIndex
            LogUtils.e(
                "当前=${gridState.firstVisibleItemIndex}，最后=${lastIndex}  " +
                        "是否加载下一页：${gridState.firstVisibleItemIndex%10==5}")
            if (current%10==5 && current > lastIndex) {
                viewModel.loadMore()
            }
            if (current>lastIndex) {
                lastIndex = current
            }
        }
    }
}


@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun PhotoItem(url: String) {

   Box(
       modifier = Modifier
           .fillMaxWidth()
           .height(240.dp)
   ) {
       val painter = rememberGlidePainter(
           request = url,
           fadeIn = true,
           previewPlaceholder = R.drawable.no_banner,
       )

       Image(
           painter = painter,
           contentDescription = "空图片",
           contentScale = ContentScale.Crop,
           modifier = Modifier
               .fillMaxWidth()
               .fillMaxHeight()
       )

       if (painter.loadState !is ImageLoadState.Success) {
           ImagePlaceholder()
//           when(painter.loadState) {
//               ImageLoadState.Loading ->
//                   CircularProgressIndicator(Modifier.align(Alignment.Center), color = HamTheme.colors.themeUi)
//               is ImageLoadState.Error -> ImagePlaceholder()
//               ImageLoadState.Empty ->ImagePlaceholder()
//           }
       }
   }

}

@Composable
fun ImagePlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.no_banner),
        contentDescription = "空图片",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}