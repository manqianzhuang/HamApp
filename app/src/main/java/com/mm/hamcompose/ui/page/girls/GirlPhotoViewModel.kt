package com.mm.hamcompose.ui.page.girls

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.mm.hamcompose.bean.Article
import com.mm.hamcompose.bean.WelfareBean
import com.mm.hamcompose.bean.WelfareData
import com.mm.hamcompose.repository.HttpRepo
import com.mm.hamcompose.ui.page.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class GirlPhotoViewModel @ViewModelInject constructor(
    private var repo: HttpRepo
): BaseViewModel<WelfareData>() {

    var page = 1
    var hasNext = true
    //var pagingData = MutableLiveData<Flow<PagingData<WelfareData>>>()
    val photos = MutableLiveData(mutableListOf<WelfareData>())

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
            repo.getWelfareData(page)
                .collectLatest {
                    if (it.results!=null) {
                        LogUtils.e("图片列表 ${it.results}")
                        hasNext = hasNext(it.results!!)
                        if (hasNext) {
                            page+=1
                        }
                        if (photos.value!!.isEmpty()) {
                            photos.value = it.results!! as MutableList<WelfareData>
                        } else {
                            photos.value?.addAll(it.results!!)
                        }
                    }
                }
        }
    }

    private fun hasNext(results: List<WelfareData>): Boolean = results.size>=20

    private fun girlPhoto() = repo.getWelfareData("福利")

}