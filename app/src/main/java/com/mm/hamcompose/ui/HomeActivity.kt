package com.mm.hamcompose.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.mm.hamcompose.R
import com.mm.hamcompose.isMainStack
import com.mm.hamcompose.ui.page.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeEntry(onBackPressedDispatcher)
        }
    }

    var cacheMills: Long = 0L
    override fun onBackPressed() {
        LogUtils.e("是否可以回退 ${onBackPressedDispatcher.hasEnabledCallbacks()}")
        if (!onBackPressedDispatcher.hasEnabledCallbacks()) {
            if (System.currentTimeMillis() - cacheMills > 1000L) {
                cacheMills = System.currentTimeMillis()
                ToastUtils.showShort("连按两次退出app")
            } else {
                this.finish()
                exitProcess(0)
            }
        }
        else super.onBackPressed()
    }
}

