package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.material.placeholder
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.HamTheme

@Composable
fun NetworkImage(
    url: String,
    isLoading: Boolean = true,
    contentDesc: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                crossfade(true)
                placeholder(R.drawable.no_banner)
            }
        ),
        contentDescription = contentDesc,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = HamTheme.colors.placeholder
            )
    )
}
