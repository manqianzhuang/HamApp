package com.mm.hamcompose.ui.page.base

import android.os.Bundle
import androidx.activity.ComponentActivity

abstract class BaseActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initData()
    }

    //abstract fun initData()
}