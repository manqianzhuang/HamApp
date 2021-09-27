package com.mm.hamcompose.ui.page.girls.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.WelfareData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.ui.route.RouteUtils.back
import com.mm.hamcompose.ui.widget.HamToolBar
import com.mm.hamcompose.ui.widget.MainTitle
import com.mm.hamcompose.ui.widget.TextContent

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GirlInfoPage(
    welfare: WelfareData,
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
) {
    Column {
        HamToolBar(title = welfare.title!!, onBack = { navCtrl.back() })
        PhotoView(welfare = welfare)
    }
}

@Composable
private fun PhotoView(welfare: WelfareData) {
    Box {
        Image(
            painter = rememberImagePainter(
                data = welfare.url,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.no_banner)

                },
            ),
            contentDescription = welfare.author,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
        MainTitle(
            title = welfare.author!!,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopStart)
        )
        TextContent(
            text = welfare.desc!!,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(HamTheme.colors.placeholder, HamTheme.colors.placeholder)
                    ),
                    alpha = 0.3f
                )
                .padding(horizontal = 10.dp)
            ,
            color = HamTheme.colors.mainColor
        )
    }

}
