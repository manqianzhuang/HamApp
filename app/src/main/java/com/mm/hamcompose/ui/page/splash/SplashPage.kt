package com.mm.hamcompose.ui.page.splash

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.R
import com.mm.hamcompose.theme.splashText
import com.mm.hamcompose.theme.white1
import com.mm.hamcompose.ui.widget.TextContent
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashPage(onNextPage: () -> Unit) {

    val imageRes = listOf(
        R.mipmap.splash_image01,
        R.mipmap.splash_image02,
        R.mipmap.splash_image03,
        R.mipmap.splash_image04,
        R.mipmap.splash_image05
    )

    val bgImage = imageRes[Random.nextInt(imageRes.size)]
    var interval by remember { mutableStateOf(5) }
    val timer = SplashTimer(
        onTickMillis = { if (interval >= 0) { interval -= 1 } },
        onFinished = { onNextPage() }
    ).start()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        val (tips, version, play, android) = createRefs()

        Image(
            painter = painterResource(id = bgImage),
            contentDescription = "背景图",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillBounds
        )

        SplashIntervalText(
            title = interval.toString(),
            modifier = Modifier
                .padding(top = 60.dp, end = 20.dp)
                .constrainAs(tips) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            doClick = {
                timer.cancel()
                onNextPage()
            })

        TextContent(
            text = "Version: 1.0.0",
            color = white1,
            modifier = Modifier
                .padding(bottom = 80.dp)
                .constrainAs(version) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "Wan",
            fontSize = 36.sp,
            color = white1,
            modifier = Modifier
                .padding(bottom = 80.dp, end = 100.dp)
                .constrainAs(play) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
        )

        Text(
            text = "Android",
            modifier = Modifier
                .padding(top = 50.dp, start = 100.dp)
                .constrainAs(android) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            fontSize = 36.sp,
            color = white1
        )

    }
}

class SplashTimer(
    val onTickMillis: () -> Unit,
    val onFinished: () -> Unit
) : CountDownTimer(5000, 1000) {
    override fun onTick(millisUntilFinished: Long) {
        LogUtils.i("onTick = $millisUntilFinished")
        onTickMillis()
    }

    override fun onFinish() {
        onFinished()
    }
}

@Composable
fun SplashIntervalText(title: String, modifier: Modifier, doClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(60.dp)
            .height(30.dp)
            .background(splashText, RoundedCornerShape(15.dp))
            .clickable {
                doClick()
            }
    ) {
        TextContent(
            text = if (title == "-1") "进入" else "${title}s",
            color = white1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}