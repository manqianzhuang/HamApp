package com.mm.hamcompose.ui.page.girls.list

import androidx.compose.runtime.mutableStateOf
import com.mm.hamcompose.data.bean.WelfareData
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class GirlPhotoViewModel @Inject constructor(
    private var repo: HttpRepository
) : BaseViewModel<WelfareData>() {

    val pageSize = 40
    var page = mutableStateOf(1)
    var hasNext = mutableStateOf(false)

    val photoData = mutableStateOf(mutableListOf<WelfareData>())

    override fun start() {
        initThat { loadContent() }
    }

    fun loadMore() {
        if (hasNext.value) {
            page.value += 1
            loadContent()
        }
    }

    override fun loadContent() {
        async {
            repo.getWelfareData(page.value, pageSize).collectLatest { response ->
                when (response) {
                    is HttpResult.Success -> {
                        val photos = response.result.data
                        if (!photos.isNullOrEmpty()) {
                            hasNext.value = true
                            if (photoData.value.isEmpty()) {
                                photoData.value = photos as MutableList<WelfareData>
                            } else {
                                photoData.value.addAll(photos)
                            }
                        } else {
                            hasNext.value = false
                        }
                    }
                    is HttpResult.Error -> {
                        println(response.exception.message)
                    }
                }
            }
        }
    }
}