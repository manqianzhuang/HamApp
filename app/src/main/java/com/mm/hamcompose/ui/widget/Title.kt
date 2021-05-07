package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mm.hamcompose.theme.HamTheme

@Composable
fun LargeTitle(title: String, modifier: Modifier = Modifier) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = 20.sp,
        color = HamTheme.colors.textPrimary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun MainTitle(title: String, modifier: Modifier = Modifier) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = 16.sp,
        color = HamTheme.colors.textPrimary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun MediumTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = HamTheme.colors.textPrimary
) {
    Title(
        title = title,
        fontSize = 16.sp,
        modifier = modifier,
        color = color
    )
}

@Composable
fun TextContent(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = HamTheme.colors.textSecondary
) {
    Title(title = title, modifier = modifier, fontSize = 14.sp, color = color)
}

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    color: Color = HamTheme.colors.textSecondary,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = title,
        modifier = modifier,
        fontSize = fontSize,
        color = color
    )
}