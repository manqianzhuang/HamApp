package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.buttonCorner
import com.mm.hamcompose.theme.buttonHeight
import com.mm.hamcompose.theme.white1
import org.jetbrains.annotations.NotNull


@Composable
fun HamButton(
    text: String,
    modifier: Modifier = Modifier,
    bgColor: Color = HamTheme.colors.secondBtnBg,
    textColor: Color = HamTheme.colors.textPrimary,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .background(color = bgColor, shape = RoundedCornerShape(buttonCorner))
            .clickable {
                onClick()
            }
    ) {
        TextContent(text = text, color = textColor, modifier = Modifier.align(Alignment.Center))
    }
}


@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    HamButton(
        text = text,
        modifier = modifier,
        textColor = HamTheme.colors.textPrimary,
        onClick = onClick,
        bgColor = HamTheme.colors.themeUi
    )
}

@Composable
fun SecondlyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    HamButton(
        text = text,
        modifier = modifier,
        textColor = HamTheme.colors.textSecondary,
        onClick = onClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelTextButton(
    @NotNull text: String,
    modifier: Modifier = Modifier,
    isSelect: Boolean = true,
    specTextColor: Color? = null,
    cornerValue: Dp = 25.dp / 2,
    onClick: (() -> Unit)? = null,
    onLongClick: (()-> Unit)? = null
) {
    Text(
        text = text,
        modifier = modifier
            .height(25.dp)
            .background(
                color = if (isSelect) HamTheme.colors.themeUi else HamTheme.colors.secondBtnBg,
                shape = RoundedCornerShape(cornerValue)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp
            )
            .combinedClickable(
                onClick = { onClick?.invoke() },
                onLongClick = { onLongClick?.invoke() }
            ),
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = specTextColor ?: if (isSelect) white1 else HamTheme.colors.textSecondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

