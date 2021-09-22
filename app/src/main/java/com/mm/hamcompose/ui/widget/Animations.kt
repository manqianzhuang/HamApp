package com.mm.hamcompose.ui.widget

import androidx.compose.animation.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadeAnim(
    isVisible: Boolean,
    content: @Composable ()-> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        content
    }
}