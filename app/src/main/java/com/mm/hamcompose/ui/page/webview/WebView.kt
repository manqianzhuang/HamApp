package com.mm.hamcompose.ui.page.webview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.blankj.utilcode.util.SizeUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mm.hamcompose.R
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.ui.widget.HamTopBar

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WebViewPage(
    data: WebData,
    backPress: () -> Unit,
) {
    var ctrl: WebViewCtrl? by remember { mutableStateOf(null) }
    Box {

        var isRefreshing: Boolean by remember { mutableStateOf(false) }
        val refreshState = rememberSwipeRefreshState(isRefreshing)
//        SwipeRefresh(
//            state = refreshState,
//            onRefresh = {
//                ctrl?.refresh()
//            },
//        ) {
        AndroidView(
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxSize(),
            factory = { context ->
                FrameLayout(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    val progressView = ProgressBar(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            SizeUtils.dp2px(2f)
                        )
                        progressDrawable =
                            context.resources.getDrawable(R.drawable.drawable_horizontal_progressbar)
                        indeterminateTintList =
                            ColorStateList.valueOf(context.resources.getColor(R.color.teal_200))
                    }
                    val webView = WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    addView(webView)
                    addView(progressView)
                    ctrl = WebViewCtrl(this, data.url!!, onWebCall = { isFinish ->
                        isRefreshing = !isFinish
                    })
                    ctrl?.initSettings()
                }

            },
            update = {

            }
        )
//        }

        HamTopBar(title = data.title!!, onBack = {
            ctrl?.onDestroy()
            backPress()
        })
    }
}