package com.mm.hamcompose.ui.page.girls

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
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
): BaseViewModel<WelfareData>() {

    var page = 1
    var hasNext = true
    //var pagingData = MutableLiveData<Flow<PagingData<WelfareData>>>()
    val photoData = MutableLiveData(mutableListOf<WelfareData>())

    override fun start() {
//        if (pagingData.value==null) {
//            pagingData.value = girlPhoto()
//        }
        if (page==1) {
            loadContent()
        }
    }

    fun loadMore() {
        loadContent()
    }

    override fun loadContent() {
        async {
            repo.getWelfareData(page).collectLatest {
                    when (it) {
                        is HttpResult.Success -> {
                            val photos = it.result.results
                            if (photos!=null) {
                                LogUtils.e("图片列表 $photos")
                                hasNext = hasNext(photos)
                                if (hasNext) {
                                    page+=1
                                }
                                if (photoData.value!!.isEmpty()) {
                                    photoData.value = photos as MutableList<WelfareData>
                                } else {
                                    photoData.value?.addAll(photos)
                                }
                            }
                        }
                        is HttpResult.Error -> {
                            
                        }
                    }
                }
        }
    }

    private fun hasNext(results: List<WelfareData>): Boolean = results.size>=20

    private fun girlPhoto() = repo.getWelfareData("福利")

}