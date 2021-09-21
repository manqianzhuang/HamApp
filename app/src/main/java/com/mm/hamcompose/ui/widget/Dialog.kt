package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.blankj.utilcode.util.SPUtils
import com.google.accompanist.insets.imePadding
import com.mm.hamcompose.theme.HamColors
import com.mm.hamcompose.theme.HamTheme
import com.mm.hamcompose.theme.THEME_COLOR_KEY
import com.mm.hamcompose.theme.themeColors

@Composable
fun SampleAlertDialog(
    title: String,
    content: String,
    cancelText: String = "取消",
    confirmText: String = "继续",
    onConfirmClick: () -> Unit,
    //onCancelClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            MediumTitle(title = title)
        },
        text = {
            TextContent(text = content)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick.invoke()
                onDismiss.invoke()
            }) {
                TextContent(text = confirmText, color = HamTheme.colors.textPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                TextContent(text = cancelText)
            }
        },
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
fun SelectAlertDialog(
    title: String,
    content: String,
    primaryButtonText: String,
    secondButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(Modifier.padding(20.dp)) {
                MediumTitle(title = title, modifier = Modifier.padding(bottom = 20.dp))
                TextContent(text = content, modifier = Modifier.padding(bottom = 20.dp))
                PrimaryButton(text = primaryButtonText, Modifier.padding(bottom = 10.dp)) {
                    onPrimaryButtonClick.invoke()
                    onDismiss.invoke()
                }
                SecondlyButton(text = secondButtonText) {
                    onSecondButtonClick.invoke()
                    onDismiss.invoke()
                }
            }
        }
    }
}

@Composable
fun InfoDialog(
    title: String = "关于我",
    vararg content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true),
        title = {
            MediumTitle(title = title)
        },
        text = {
            Column(
                Modifier.defaultMinSize(minWidth = 300.dp)
            ) {
                content.forEach {
                    TextContent(
                        text = it,
                        modifier = Modifier.padding(bottom = 10.dp),
                        canCopy = true
                    )
                }
            }
        },
        confirmButton = {
            TextContent(
                text = "关闭",
                modifier = Modifier
                    .padding(end = 18.dp, bottom = 18.dp)
                    .clickable { onDismiss.invoke() }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaletteSelectorDialog(
    initKey: Int,
    title: String = "选择主题",
    confirmText: String = "关闭",
    onDismiss: () -> Unit = {},
    onSelectItem: (index: Int)-> Unit,
) {

    AlertDialog(
        title = {
            MediumTitle(title = title)
        },
        text = {
            LazyVerticalGrid(
                cells = GridCells.Fixed(4),
            ) {
                itemsIndexed(themeColors) { index, color ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .wrapContentSize()
                            .background(HamTheme.colors.mainColor, RoundedCornerShape(24.dp))
                            .clickable {
                                SPUtils.getInstance().put(THEME_COLOR_KEY, index)
                                onSelectItem.invoke(index)
                                onDismiss.invoke()
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .align(Alignment.Center)
                                .clip(RoundedCornerShape(24.dp))
                                .background(color = color)
                        )
                        if (index == initKey) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "done",
                                tint = HamTheme.colors.mainColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDismiss.invoke()
            }) {
                TextContent(text = confirmText, color = HamTheme.colors.textPrimary)
            }
        },
        modifier = Modifier
            //.padding(horizontal = 30.dp)
            .defaultMinSize(minWidth = 300.dp)
            .wrapContentSize()
            .background(HamTheme.colors.mainColor)
    )
}
