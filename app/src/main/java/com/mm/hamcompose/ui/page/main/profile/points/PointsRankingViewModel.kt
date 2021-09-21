package com.mm.hamcompose.ui.page.main.profile.points

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mm.hamcompose.data.bean.PointsBean
import com.mm.hamcompose.data.bean.TabTitle
import com.mm.hamcompose.data.http.HttpResult
import com.mm.hamcompose.repository.HttpRepository
import com.mm.hamcompose.repository.PagingPoints
import com.mm.hamcompose.ui.page.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class PointsRankingViewModel @Inject constructor(
    private val repo: HttpRepository,
) : BaseViewModel<PointsBean>() {

    val pagingRanking = MutableLiveData<PagingPoints?>(null)
    val pagingRecords = MutableLiveData<PagingPoints?>(null)
    val personalPoints = mutableStateOf<PointsBean?>(null)
    var tabIndex = mutableStateOf(0)
    var errorMessage = mutableStateOf<String?>(null)
    val titles = mutableStateOf(mutableListOf(
        TabTitle(501, "排行榜"),
        TabTitle(502, "我的积分"),
    ))

    override fun start() {
        initThat { fetchData() }
    }

    private fun fetchData() {
        if (personalPoints.value==null) {
            requestPersonPoints()
        }
        if (pagingRanking.value == null) {
            pagingRanking.value = ranking()
        }
        if (pagingRecords.value == null) {
            pagingRecords.value = records()
        }
    }

    private fun requestPersonPoints() {
        async {
            repo.getMyPointsRanking()
                .collectLatest { response ->
                    when(response) {
                        is HttpResult.Success -> { personalPoints.value = response.result }
                        is HttpResult.Error -> { errorMessage.value = response.exception.message }
                    }
                }
        }
    }

    private fun ranking() = repo.getPointsRankings().cachedIn(viewModelScope)

    private fun records() = repo.getPointsRecords().cachedIn(viewModelScope)

}