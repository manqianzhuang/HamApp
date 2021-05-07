package com.mm.hamcompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mm.hamcompose.theme.white1

@Composable
fun LabelView(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier
            .background(color = Color.Red, shape = RoundedCornerShape(18.dp / 2))
            .size(40.dp, 18.dp),
//            .padding(vertical = 3.dp),
        textAlign = TextAlign.Center,
        color = white1,
        fontSize = 12.sp
    )
}