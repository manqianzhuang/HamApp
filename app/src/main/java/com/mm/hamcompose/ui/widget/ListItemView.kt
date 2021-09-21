package com.mm.hamcompose.ui.widget

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mm.hamcompose.R
import com.mm.hamcompose.data.bean.Article
import com.mm.hamcompose.data.bean.CollectBean
import com.mm.hamcompose.data.bean.WebData
import com.mm.hamcompose.theme.*
import com.mm.hamcompose.util.RegexUtils

@Composable
fun ListTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListTitleHeight)
            .background(color = HamTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(5.dp)
                .height(16.dp)
                .align(Alignment.CenterVertically)
                .background(color = HamTheme.colors.textPrimary)
        )
        MediumTitle(
            title = title,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CollectListItemView(
    item: CollectBean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentWidth()
            .clickable { onClick.invoke() },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(white1)
                .padding(20.dp),
        ) {
            val (name, publishIcon, publishTime, title, delete) = createRefs()
            Text(
                text = if (item.author.isNotEmpty()) item.author else "无名作者",
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }

            )
            Text(
                text = item.niceDate,
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
                text = item.title,
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
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = HamTheme.colors.textSecondary,
                modifier = Modifier
                    .constrainAs(delete) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        onDeleteClick.invoke()
                    }
                    .pointerInteropFilter { false }
            )
        }
    }
}

@Composable
fun SimpleListItemView(data: Article, onClick: () -> Unit, onCollectClick: (articleId: Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentWidth()
            .clickable {
                onClick.invoke()
            },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(white1)
                .padding(20.dp),
        ) {
            val (name, publishIcon, publishTime, title, favourite) = createRefs()
            Text(
                text = data.author ?: data.shareUser ?: "作者",
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
                        top.linkTo(name.bottom)
                        end.linkTo(parent.end)
                    },
                color = HamTheme.colors.textSecondary
            )
            FavouriteIcon(
                modifier = Modifier.constrainAs(favourite) {
                    top.linkTo(title.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                isFavourite = data.collect,
                onClick = {
                    onCollectClick.invoke(data.id)
                }
            ) }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MultiStateItemView(
    modifier: Modifier = Modifier,
    data: Article,
    isTop: Boolean = false,
    onSelected: (data: WebData) -> Unit,
    onCollectClick: (articleId: Int) -> Unit,
    onUserClick: (userId: Int) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .clickable {
                val webData = WebData(data.title!!, data.link!!)
                onSelected(webData)
            },
        shape = HamShapes.medium,
        backgroundColor = HamTheme.colors.listItem,
    ) {
        Box {
            ConstraintLayout(
                modifier = Modifier
                    .background(white1)
                    .padding(20.dp),
            ) {
                val (circleText, name, publishIcon, publishTime, title, chip1, chip2, tag, favourite) = createRefs()
                Text(
                    text = getFirstCharFromName(data),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .background(HamTheme.colors.themeUi, shape = RoundedCornerShape(20.dp / 2))
                        .padding(vertical = 1.dp)
                        .constrainAs(circleText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                    textAlign = TextAlign.Center,
                    fontSize = H7,
                    color = white1
                )
                MediumTitle(
                    title = getAuthorName(data),
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(parent.top)
                            start.linkTo(circleText.end)
                        }
                        .padding(start = 5.dp)
                        .clickable {
                            onUserClick.invoke(data.userId)
                        }
                        .pointerInteropFilter { false }
                )
                MiniTitle(
                    text = RegexUtils().timestamp(data.niceDate!!) ?: "1970-1-1",
                    modifier = Modifier.constrainAs(publishTime) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                )
                TimerIcon(
                    modifier = Modifier.constrainAs(publishIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(publishTime.start)
                        bottom.linkTo(publishTime.bottom)
                    }
                )
                TextContent(
                    text = data.title ?: "这是标题",
                    maxLines = 3,
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
                LabelTextButton(
                    text = data.superChapterName ?: "热门",
                    modifier = Modifier
                        .constrainAs(chip1) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                )
                LabelTextButton(
                    text = data.chapterName ?: "android",
                    modifier = Modifier
                        .constrainAs(chip2) {
                            top.linkTo(title.bottom)
                            start.linkTo(chip1.end, margin = 5.dp)
                            bottom.linkTo(parent.bottom)
                        },
                )
                FavouriteIcon(
                    modifier = Modifier.constrainAs(favourite) {
                        top.linkTo(title.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    isFavourite = data.collect,
                    onClick = {
                        onCollectClick.invoke(data.id)
                    }
                )
//                ShareIcon(
//                    modifier = Modifier
//                        .padding(end = 5.dp)
//                        .constrainAs(share) {
//                            top.linkTo(title.bottom)
//                            end.linkTo(follow.start)
//                            bottom.linkTo(parent.bottom)
//                        }
//                )
                Row(
                    modifier = Modifier.constrainAs(tag) {
                        top.linkTo(parent.top)
                        start.linkTo(name.end, margin = 5.dp)
                    }
                ) {
                    if (isTop) {
                        HotIcon()
                    }
                    if (data.fresh) {
                        TagView(
                            tagText = "最新",
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ArrowRightListItem(
    iconRes: Any,
    title: String,
    msgCount: Int? = null,
    valueText: String = "",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp)
            .clickable {
                onClick.invoke()
            }
    ) {

        when (iconRes) {
            is Painter -> {
                Icon(
                    painter = iconRes,
                    contentDescription = null,
                    tint = HamTheme.colors.icon,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = iconRes,
                    contentDescription = null,
                    tint = HamTheme.colors.icon,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            TextContent(text = title, modifier = Modifier.align(Alignment.CenterVertically))
            if (msgCount != null ) {
                Text(
                    text = "（$msgCount）",
                    fontSize = H7,
                    color = HamTheme.colors.error,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        if (valueText.isNotEmpty()) {
            TextContent(
                text = valueText,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterVertically))
        }
        Icon(
            Icons.Default.KeyboardArrowRight,
            null,
            tint = HamTheme.colors.textSecondary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
    Divider()
}


fun getAuthorName(data: Article): String {
    val emptyAuthor = "作者"
    return if (TextUtils.isEmpty(data.author)) {
        data.shareUser ?: emptyAuthor
    } else {
        data.author ?: emptyAuthor
    }
}

fun getFirstCharFromName(data: Article): String {
    return getAuthorName(data).trim().substring(0, 1)
}


