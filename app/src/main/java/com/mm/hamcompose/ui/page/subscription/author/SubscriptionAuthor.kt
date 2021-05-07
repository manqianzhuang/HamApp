package com.mm.hamcompose.ui.page.subscription.author

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.HamRouter
import com.mm.hamcompose.ui.RouteActions
import com.mm.hamcompose.ui.page.subscription.category.SubscriptionViewModel
import com.mm.hamcompose.ui.widget.HamTopBar

@Composable
fun SubscriptionAuthorPage(
    parent: ParentBean,
    actions: RouteActions,
) {

    val viewModel = viewModel(SubscriptionAuthorViewModel::class.java)
    viewModel.setPublicId(parent.id)
    viewModel.start()

    Box {

        val articles = viewModel.publicData.value?.collectAsLazyPagingItems()
        val refreshing = viewModel.isRefreshing.observeAsState()
        val swipeRefreshState = rememberSwipeRefreshState(refreshing.value!!)
        viewModel.initPublicArticles()

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                LogUtils.e("刷新数据")
                viewModel.refresh()
            },
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp)
        ) {
            if (articles != null) {
                LazyColumn {
                    items(articles) { item ->
                        SubscriptionItem(item!!, actions)
                    }
                }
            }
        }

        HamTopBar(
            title = parent.name ?: "公众号详情",
            onBack = {
                actions.backPress()
            },
            onSearch = {
                actions.selected(HamRouter.subscriptionSearch, parent)
            })
    }
}

@Composable
fun SubscriptionItem(item: Article, actions: RouteActions) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentWidth()
            .clickable {
                val webData = WebData(item.title, item.link!!)
                actions.selected(HamRouter.webView, webData)
            },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(white1)
                .padding(20.dp),
        ) {
            val (name, publishIcon, publishTime, title, share, follow) = createRefs()
            Text(
                text = item.author ?: item.shareUser ?: "作者",
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 5.dp)
            )
            Text(
                text = item.niceDate?:"1970-1-1",
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
                text = item.title?:"这是标题",
                fontSize = 15.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 20.dp)
                    .constrainAs(title) {
                        top.linkTo(name.bottom)
                        end.linkTo(parent.end)
                    },
                color = HamTheme.colors.textSecondary
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
        }
    }
}