package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsPadding
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.white1
import javax.annotation.Nullable

/**
 * 普通标题栏头部
 */
@Composable
fun HamTopBar(
    title: String,
    rightText: String? = null,
    onBack: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(HamApp.CONTEXT.resources.getColor(R.color.teal_200)))
            .statusBarsPadding()
    ) {
        Row(Modifier.height(48.dp)) {
            if (onBack != null) {
                Icon(
                    painterResource(R.drawable.icon_back_white),
                    null,
                    Modifier
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .size(36.dp)
                        .padding(8.dp),
                    tint = HamTheme.colors.onBadge
                )
            }
            Spacer(Modifier.weight(1f))
            if (!rightText.isNullOrEmpty()) {
                Text(
                    rightText,
                    color = HamTheme.colors.onBadge,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(10.dp)
                )
            }
            if (onSearch != null) {
                Icon(
                    painterResource(R.drawable.ic_search),
                    null,
                    Modifier
                        .clickable(onClick = onSearch)
                        .align(Alignment.CenterVertically)
                        .size(25.dp)
                        .padding(end = 8.dp),
                    tint = HamTheme.colors.onBadge
                )
            }
        }
        Text(
            title,
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 30.dp),
            fontSize = 18.sp,
            color = HamTheme.colors.onBadge,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * TabLayout
 */
@Composable
fun TopTabBar(
    index: Int,
    tabTexts: MutableList<String>,
    modifier: Modifier = Modifier,
    bgColor: Color = HamTheme.colors.themeUi,
    contentColor: Color = Color.White,
    onTabSelected: ((index: Int) -> Unit)? = null
) {

//    ScrollableTabRow(
//        selectedTabIndex = index,
//        modifier = modifier.height(48.dp),
//        edgePadding = 0.dp,
//        backgroundColor = bgColor,
//        contentColor = contentColor,
//    ) {
    var offset: Float by remember{ mutableStateOf(0f) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(bgColor)
            .horizontalScroll(state = rememberScrollState(),)
    ) {
        tabTexts.forEachIndexed { i, title ->
//            Tab(
//                selected = index == i,
//                onClick = {
//                    if (onTabSelected != null) {
//                        onTabSelected(i)
//                    }
//                },
//                modifier = Modifier.fillMaxHeight()
//            ) {
            Text(
                text = title,
                fontSize = if (index == i) 20.sp else 15.sp,
                fontWeight = if (index == i) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
                    .clickable {
                        if (onTabSelected != null) {
                            onTabSelected(i)
                        }
                    },
                color = contentColor
            )
//            }
        }
    }
}


@Composable
fun LabelText(
    @Nullable text: String,
    modifier: Modifier = Modifier,
    isSelect: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Text(
        text = text,
        modifier = modifier
            .height(25.dp)
            .background(
                color = if (isSelect) HamTheme.colors.themeUi else HamTheme.colors.buttonBg,
                shape = RoundedCornerShape(25.dp / 2)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp
            )
            .clickable {
                if (onClick != null) {
                    onClick()
                }
            },
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = if (isSelect) white1 else HamTheme.colors.textSecondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}