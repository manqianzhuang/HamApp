package com.mm.hamcompose.ui.page.index

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamShapes
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.Teal200
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.widget.LabelText
import com.mm.hamcompose.ui.widget.LabelView


@Composable
fun IndexPage(
    onSelected: (routeName: String, data: WebData) -> Unit,
    viewModel: IndexViewModel = viewModel(IndexViewModel::class.java)
) {

    LogUtils.e("刷新主页")

    viewModel.start()
    val homeData = viewModel.pagingData.value?.collectAsLazyPagingItems()
    val refreshing = viewModel.isRefreshing.observeAsState()
    val topArticle = viewModel.topArticles.observeAsState()
    val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        LogUtils.e("刷新数据")
        viewModel.isRefreshing.value = true
        viewModel.pagingData.value = null
        viewModel.start()
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(top = 10.dp)
        ) {
            if (topArticle.value!!.isNotEmpty()) {
                topArticle.value!!.forEach { item ->
                    item {
                        IndexItem(item, true, onSelected = onSelected)
                    }
                }
            }
            if (homeData != null) {
                items(homeData) { indexBean ->
                    IndexItem(indexBean!!, onSelected = onSelected)
                }
            }
        }
    }
}

@Composable
fun IndexItem(
    data: Article,
    isTop: Boolean = false,
    onSelected: (routeName: String, data: WebData) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentWidth()
            .clickable {
                val webData = WebData(data.title, data.link!!)
                onSelected(HamRouter.webView, webData)
            },
        shape = HamShapes.medium,
        backgroundColor = HamTheme.colors.onBadge,
        //border = BorderStroke(1.dp, HamTheme.colors.divider)
    ) {
        Box {

            ConstraintLayout(
                modifier = Modifier
                    .background(white1)
                    .padding(20.dp),
            ) {
                val (circleText, name, publishIcon, publishTime, title, chip1, chip2, topChip, share, follow) = createRefs()
                Text(
                    text = getFirstCharFromName(data),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .background(Teal200, shape = RoundedCornerShape(20.dp / 2))
                        .padding(vertical = 1.dp)
                        .constrainAs(circleText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = white1
                )
                Text(
                    text = getAuthorName(data),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(parent.top)
                            start.linkTo(circleText.end)
                        }
                        .padding(start = 5.dp)
                )
                Text(
                    text = data.niceDate ?: "1970-1-1",
                    fontSize = 13.sp,
                    modifier = Modifier.constrainAs(publishTime) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }

                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                        .constrainAs(publishIcon) {
                            top.linkTo(parent.top, margin = 2.5.dp)
                            end.linkTo(publishTime.start)
                        }
                )
                Text(
                    text = data.title ?: "这是标题",
                    fontSize = 15.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp, bottom = 20.dp)
                        .constrainAs(title) {
                            top.linkTo(circleText.bottom)
                            end.linkTo(parent.end)
                        },
                    color = HamTheme.colors.textSecondary
                )
                LabelText(
                    text = data.superChapterName ?: "热门",
                    modifier = Modifier
                        .constrainAs(chip1) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                )
                LabelText(
                    text = data.chapterName ?: "android",
                    modifier = Modifier
                        .constrainAs(chip2) {
                            top.linkTo(title.bottom)
                            start.linkTo(chip1.end, margin = 5.dp)
                            bottom.linkTo(parent.bottom)
                        },
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .constrainAs(follow) {
                            top.linkTo(title.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .padding(end = 5.dp)
                        .constrainAs(share) {
                            top.linkTo(title.bottom)
                            end.linkTo(follow.start)
                            bottom.linkTo(parent.bottom)
                        }
                )
                if (isTop) {
                    LabelView(
                        title = "置顶",
                        modifier = Modifier.constrainAs(topChip) {
                            top.linkTo(parent.top)
                            start.linkTo(name.end, margin = 5.dp)
                        })
                }
            }

        }
    }

}

fun getAuthorName(data: Article): String {
    val emptyAuthor = "SuperHam"
    return if (TextUtils.isEmpty(data.author)) {
        data.shareUser ?: emptyAuthor
    } else {
        data.author ?: emptyAuthor
    }
}

fun getFirstCharFromName(data: Article): String {
    return getAuthorName(data).trim().substring(0, 1)
}

