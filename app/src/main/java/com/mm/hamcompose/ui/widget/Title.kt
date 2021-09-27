package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.google.accompanist.placeholder.material.placeholder
import com.mm.hamcompose.theme.*

@Composable
fun LargeTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = H3,
        color = color ?: HamTheme.colors.textPrimary,
        fontWeight = FontWeight.Bold,
        isLoading = isLoading
    )
}

@Composable
fun MainTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = HamTheme.colors.textPrimary,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = H4,
        color = color,
        fontWeight = FontWeight.SemiBold,
        maxLine = maxLine,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
fun MediumTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = HamTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        fontSize = H5,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
fun TextContent(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = HamTheme.colors.textSecondary,
    maxLines: Int = 99,
    textAlign: TextAlign = TextAlign.Start,
    canCopy: Boolean = false,
    isLoading: Boolean = false
) {
    if (canCopy) {
        SelectionContainer {
            Title(
                title = text,
                modifier = modifier,
                fontSize = H6,
                color = color,
                maxLine = maxLines,
                textAlign = textAlign,
                isLoading = isLoading
            )
        }
    } else {
        Title(
            title = text,
            modifier = modifier,
            fontSize = H6,
            color = color,
            maxLine = maxLines,
            textAlign = textAlign,
            isLoading = isLoading
        )
    }

}

@Composable
fun MiniTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = HamTheme.colors.textSecondary,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = text,
        modifier = modifier,
        fontSize = H7,
        color = color,
        maxLine = maxLines,
        textAlign = textAlign,
        isLoading = isLoading,
    )
}

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    color: Color = HamTheme.colors.textSecondary,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = title,
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = HamTheme.colors.placeholder
            ),
        fontSize = fontSize,
        color = color,
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}