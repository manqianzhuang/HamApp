package com.mm.hamcompose.ui.page.main.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.MY_USER_ID
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.*
import com.mm.hamcompose.ui.route.RouteName
import com.mm.hamcompose.ui.route.RouteUtils
import com.mm.hamcompose.ui.widget.*

private const val MAX_SIZE = 10

@Composable
fun ProfilePage(
    navCtrl: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    viewModel.start()
    val isLogin by remember { viewModel.isLogin }
    val messageCount by remember { viewModel.messageCount }
    var clickJoinUs by remember { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(viewModel.isRefresh.value)
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .padding(bottom = BottomNavBarHeight)
            .fillMaxSize()
            .background(color = HamTheme.colors.mainColor)
    ) {

        if (isLogin && viewModel.userInfo.value != null) {
            SwipeRefresh(
                state = refreshState,
                onRefresh = { viewModel.refresh() }
            ) {
                LazyColumn(state = listState) {
                    item {
                        HeaderPart(
                            navCtrl = navCtrl,
                            viewModel = viewModel,
                            messageCount = messageCount
                        )
                    }
                    item {
                        ContentPart(
                            navCtrl = navCtrl,
                            viewModel = viewModel,
                            modifier = Modifier.defaultMinSize(minHeight = 200.dp)
                        )
                    }
                    item {
                        FooterPart(
                            navCtrl = navCtrl,
                            messageCount = messageCount,
                            onJoinUsClick = {
                                clickJoinUs = true
                            })
                    }
                }
            }

        } else {
            HamToolBar(title = "我的")
            EmptyView(
                tips = "点击登录",
                imageVector = Icons.Default.Face
            ) {
                RouteUtils.navTo(navCtrl, RouteName.LOGIN)
            }
        }
    }

    if (clickJoinUs) {
        InfoDialog(
            title = "加入我们",
            content = arrayOf(
                "开源作者: 鸿洋",
                "作者博客: https://blog.csdn.net/lmj623565791",
                "公众号 https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzAxMTI4MTkwNQ==&scene=124#wechat_redirect",
                "QQ交流群: 591683946",
            ),
            onDismiss = {
                clickJoinUs = false
            }
        )
    }


}

//用户信息
@Composable
fun HeaderPart(
    navCtrl: NavHostController,
    viewModel: ProfileViewModel,
    messageCount: Int,
) {
    val userInfo by remember { viewModel.userInfo }
    val myPoints by remember { viewModel.myPoints }

    Column {
        ProfileToolBar(
            msgCount = messageCount,
            onMessageIconClick = {
                RouteUtils.navTo(navCtrl, RouteName.MESSAGE)
            },
            onDeleteIconClick = {
                RouteUtils.navTo(navCtrl, RouteName.SETTINGS)
            },
            onDashboardIconClick = {
                RouteUtils.navTo(navCtrl, RouteName.SETTINGS)
            },
        )
        UserInfoItem(myPoints, userInfo)
        UserOptionsItem(
            onCollectClick = {
                try {
                    navCtrl.navigate(RouteName.COLLECTION) {
                        popUpTo(navCtrl.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            onMyShareClick = {
                RouteUtils.navTo(navCtrl, RouteName.SHARER, MY_USER_ID)
            },
            onHistoryClick = {
                RouteUtils.navTo(navCtrl, RouteName.HISTORY)
            },
            onRankingClick = {
                RouteUtils.navTo(navCtrl, RouteName.RANKING)
            }
        )
    }
}


//我的文章
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentPart(navCtrl: NavHostController, viewModel: ProfileViewModel, modifier: Modifier) {
    val myArticles by remember { viewModel.myArticles }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        if (myArticles.isNotEmpty()) {
            Column {
                ListTitle(title = "我的文章")
                var newList = myArticles
                if (myArticles.size > MAX_SIZE) {
                    newList = myArticles.subList(0, 9)
                }
                newList.forEachIndexed { index, article ->
                    TextContent(
                        text = "${index + 1}. ${article.title}",
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp,
                                top = if (index == 0) 10.dp else 0.dp
                            )
                            .clickable {
                                RouteUtils.navTo(
                                    navCtrl = navCtrl,
                                    destinationName = RouteName.WEB_VIEW,
                                    args = WebData(article.title, article.link ?: "")
                                )
                            },
                    )
                }
            }
        } else {
            ListTitle(title = "我的文章")
            Column(
                modifier = Modifier
                    .padding(top = ListTitleHeight)
                    .align(Alignment.Center)
                    .clickable {
                        RouteUtils.navTo(navCtrl, RouteName.SHARE_ARTICLE)
                    }
                    .animateContentSize()
            ) {
                AddIcon(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .align(Alignment.CenterHorizontally),
                    color = HamTheme.colors.textSecondary
                )
                TextContent(text = "添加文章")
            }
        }
    }
}

//基本操作
@Composable
fun FooterPart(
    navCtrl: NavHostController,
    messageCount: Int,
    onJoinUsClick: () -> Unit,
) {

    Column(
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        ArrowRightListItem(
            iconRes = painterResource(R.drawable.ic_message),
            title = "消息",
            msgCount = messageCount
        ) {
            RouteUtils.navTo(navCtrl, RouteName.MESSAGE)
        }
        ArrowRightListItem(
            iconRes = painterResource(R.drawable.ic_menu_settings),
            title = "设置"
        ) {
            RouteUtils.navTo(navCtrl, RouteName.SETTINGS)
        }
        ArrowRightListItem(
            iconRes = painterResource(R.drawable.ic_feedback),
            title = "WanAndroid"
        ) {
            RouteUtils.navTo(
                navCtrl = navCtrl,
                destinationName = RouteName.WEB_VIEW,
                args = WebData(title = "官方网站", url = "https://www.wanandroid.com/index")
            )
        }
        ArrowRightListItem(
            iconRes = painterResource(R.drawable.ic_data),
            title = "积分规则"
        ) {
            RouteUtils.navTo(
                navCtrl = navCtrl,
                destinationName = RouteName.WEB_VIEW,
                args = WebData(title = "积分规则", url = "https://www.wanandroid.com/blog/show/2653")
            )
        }
        ArrowRightListItem(
            iconRes = painterResource(R.drawable.ic_community),
            title = "加入我们"
        ) {
            onJoinUsClick.invoke()
        }
    }
}

//调色板、清理缓存、消息的ToolBar
@Composable
private fun ProfileToolBar(
    msgCount: Int,
    onMessageIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onDashboardIconClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(HamTheme.colors.themeUi)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                NotificationIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMessageIconClick.invoke() }
                )
                if (msgCount > 0) {
                    DotView(modifier = Modifier.align(Alignment.TopEnd))
                }
            }
            Icon(
                Icons.Default.Delete,
                contentDescription = "Clear cache",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        onDeleteIconClick.invoke()
                    },
                tint = white
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_theme),
                contentDescription = "Switch theme",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onDashboardIconClick.invoke()
                    },
                tint = white
            )
        }
    }
}

//用户基本信息
@Composable
private fun UserInfoItem(myPoints: PointsBean?, userInfo: UserInfo?) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(HamTheme.colors.themeUi)
    ) {

        val (icon, info) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.wukong),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 20.dp)
                .width(48.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .placeholder(
                    visible = userInfo == null,
                    color = HamTheme.colors.placeholder
                )
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp, bottom = 20.dp)
                .constrainAs(info) {
                    start.linkTo(icon.end)
                    top.linkTo(parent.top)
                }
        ) {
            MainTitle(
                title = userInfo?.username ?: "",
                modifier = Modifier.placeholder(
                    visible = userInfo == null,
                    color = HamTheme.colors.placeholder
                )
            )
            if (userInfo?.email?.isNotEmpty() == true) {
                MiniTitle(
                    text = "email: ${userInfo.email}",
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                TagView(
                    tagText = "Lv${myPoints?.level ?: "99"}",
                    tagBgColor = HamTheme.colors.themeUi,
                    borderColor = HamTheme.colors.textSecondary,
                    isLoading = myPoints == null
                )
                TagView(
                    modifier = Modifier.padding(start = 5.dp),
                    tagText = "积分${myPoints?.coinCount ?: "10086"}",
                    tagBgColor = HamTheme.colors.themeUi,
                    borderColor = HamTheme.colors.textSecondary,
                    isLoading = myPoints == null
                )
            }
        }
    }
}

//我的收藏、我的分享、历史记录、积分排行榜
@Composable
private fun UserOptionsItem(
    onCollectClick: () -> Unit,
    onMyShareClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onRankingClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 20.dp)
    ) {
        ProfileOptionItem(
            modifier = Modifier.weight(1f),
            title = "我的收藏",
            iconRes = Icons.Default.FavoriteBorder
        ) {
            onCollectClick.invoke()
        }
        ProfileOptionItem(
            modifier = Modifier.weight(1f),
            title = "我的文章",
            iconRes = painterResource(R.drawable.ic_article)
        ) {
            onMyShareClick.invoke()
        }
        ProfileOptionItem(
            modifier = Modifier.weight(1f),
            title = "历史浏览",
            iconRes = painterResource(R.drawable.ic_history_record)
        ) {
            onHistoryClick.invoke()
        }
        ProfileOptionItem(
            modifier = Modifier.weight(1f),
            title = "积分排行",
            iconRes = painterResource(R.drawable.ic_ranking)
        ) {
            onRankingClick.invoke()
        }
    }
}

@Composable
private fun ProfileOptionItem(
    modifier: Modifier,
    title: String,
    iconRes: Any,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick() }) {
        when (iconRes) {
            is Painter -> {
                Icon(
                    painter = iconRes,
                    contentDescription = null,
                    tint = HamTheme.colors.icon,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = iconRes,
                    contentDescription = null,
                    tint = HamTheme.colors.icon,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        MiniTitle(
            text = title,
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.CenterHorizontally)
        )
    }

}





