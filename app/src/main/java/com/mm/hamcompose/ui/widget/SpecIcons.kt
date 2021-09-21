package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.white

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HotIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_hot),
        contentDescription = null,
        tint = HamTheme.colors.hot,
        modifier = modifier
            .size(20.dp)
            .pointerInteropFilter { false }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_share),
        contentDescription = null,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .pointerInteropFilter { false }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavouriteIcon(
    modifier: Modifier,
    isFavourite: Boolean = false,
    onClick: () -> Unit
) {
    Icon(
        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = if (isFavourite) HamTheme.colors.themeUi else HamTheme.colors.textSecondary,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .clickable { onClick.invoke() }
            .pointerInteropFilter { false }
    )
}

@Composable
fun TimerIcon(
    modifier: Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_time),
        contentDescription = "",
        modifier = modifier
            .width(15.dp)
            .height(15.dp)
    )
}

@Composable
fun UserIcon(
    modifier: Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_author),
        contentDescription = "",
        modifier = modifier
            .width(15.dp)
            .height(15.dp),
        tint = HamTheme.colors.textSecondary
    )
}

@Composable
fun AddIcon(
    modifier: Modifier
) {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        tint = HamTheme.colors.textPrimary,
        modifier = modifier
    )
}

@Composable
fun NotificationIcon(
    modifier: Modifier,
    tintColor: Color = white
) {
    Icon(
        Icons.Default.Notifications,
        contentDescription = "New message",
        modifier = modifier,
        tint = white
    )
}

@Composable
fun DotView(
    modifier: Modifier,
) {
    Text(
        text = "",
        modifier = modifier
            .size(10.dp)
            .background(color = HamTheme.colors.hot, RoundedCornerShape(5.dp)),
        color = white,
        textAlign = TextAlign.Center,
        maxLines = 1,
        fontSize = 5.sp
    )
}