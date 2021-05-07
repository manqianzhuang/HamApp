package com.mm.hamcompose.ui.page.project.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.blankj.utilcode.util.LogUtils
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.Teal200
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.widget.HamTopBar

@Composable
fun ProjectDetailPage(parent: ParentBean, actions: RouteActions) {

    val viewModel = viewModel(ProjectCategoryViewModel::class.java)
    viewModel.setCid(parent.id)
    viewModel.refresh()

    Box {
        val projects = viewModel.pagingData.value?.collectAsLazyPagingItems()
        val refreshing = viewModel.isRefreshing.observeAsState()
        val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                LogUtils.e("刷新数据")
                viewModel.refresh()
            },
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp)
        ) {
            if (projects!=null) {
                LazyColumn {
                    items(projects) { item ->
                        ProjectItem(item!!, actions)
                    }
                }
            }
        }

        HamTopBar(title = parent.name ?: "项目详情", onBack = actions.backPress)
    }
}

@Composable
fun ProjectItem(project: Article, actions: RouteActions) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                val webData = WebData(project.title, project.link!!)
                actions.selected(HamRouter.webView, webData)
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Teal200)
                    .padding(20.dp)
            ) {
                Text(
                    text = project.title!!,
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = white1,
                    modifier = Modifier.align(alignment = Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Row {
                Box(modifier = Modifier.width(100.dp)) {
                    val painter = rememberCoilPainter(
                        request = project.envelopePic!!,
                        fadeIn = true,
                        previewPlaceholder = R.drawable.no_banner
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Project Image",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                    if (painter.loadState == ImageLoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = HamTheme.colors.themeUi
                        )
                    }
                }
                Column(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Text(
                        text = project.desc!!,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 7,
                        overflow = TextOverflow.Ellipsis,
                        color = HamTheme.colors.textSecondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Row(modifier = Modifier.weight(1f)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_author),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(18.dp)
                                    .padding(top = 2.dp),
                                tint = HamTheme.colors.textSecondary
                            )
                            Text(
                                text = project.author!!,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = HamTheme.colors.textSecondary,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                        }

                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_time),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp)
                                    .padding(top = 2.dp),
                                tint = HamTheme.colors.textSecondary

                            )
                            Text(
                                text = project.niceDate!!,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = HamTheme.colors.textSecondary,
                                modifier = Modifier.width(80.dp),
                                maxLines = 1,
                            )
                        }

                    }
                }
            }
        }
    }
}
