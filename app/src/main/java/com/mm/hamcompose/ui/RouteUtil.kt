/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mm.hamcompose.ui

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.blankj.utilcode.util.LogUtils
import com.mm.hamcompose.bean.ParentBean
import com.mm.hamcompose.bean.WebData
import com.mm.hamcompose.isMainStack
import com.mm.hamcompose.util.Navigator
import kotlinx.parcelize.Parcelize

/**
 * Models the screens in the app and any arguments they require.
 */
sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Parcelize
    object ArticleSearch : Destination()

    @Immutable
    @Parcelize
    data class ProjectDetail(val parent: ParentBean) : Destination()

    @Immutable
    @Parcelize
    data class SystemCategory(val parent: ParentBean) : Destination()

    @Immutable
    @Parcelize
    data class SubscribeDetail(val parent: ParentBean) : Destination()

    @Immutable
    @Parcelize
    data class SubscribeSearch(val parent: ParentBean) : Destination()

    @Immutable
    @Parcelize
    data class WebViewBrowser(val webData: WebData) : Destination()

    @Immutable
    @Parcelize
    object GirlPhoto : Destination()

}

object HamRouter{
    const val systemCategory = "system_category"
    const val projectDetail = "project_detail"
    const val articleSearch = "article_search"
    const val girlPhoto = "girl_photo"
    const val subscriptionDetail = "subscription_detail"
    const val subscriptionSearch = "subscription_search"
    const val webView = "web_view"
}

/**
 * Models the navigation actions in the app.
 */
class RouteActions(navigator: Navigator<Destination>) {

    val selected: (String, Any?) -> Unit = { which, parent ->
        val destination = when(which) {
            HamRouter.articleSearch   -> Destination.ArticleSearch
            HamRouter.projectDetail   -> Destination.ProjectDetail(parent!! as ParentBean)
            HamRouter.systemCategory   -> Destination.SystemCategory(parent!! as ParentBean)
            HamRouter.subscriptionDetail -> Destination.SubscribeDetail(parent!! as ParentBean)
            HamRouter.subscriptionSearch -> Destination.SubscribeSearch(parent!! as ParentBean)
            HamRouter.webView -> Destination.WebViewBrowser(parent!! as WebData)
            HamRouter.girlPhoto -> Destination.GirlPhoto
            else -> Destination.Home
        }
        navigator.navigate(destination)
    }

    val backPress: (() -> Unit) = {
        navigator.back()
    }
}
