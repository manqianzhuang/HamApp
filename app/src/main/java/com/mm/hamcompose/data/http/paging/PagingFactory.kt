package com.mm.hamcompose.data.http.paging

import androidx.paging.PagingConfig

class PagingFactory {

    val pagingConfig = PagingConfig(

        // 每页显示的数据的大小
        pageSize = 20,
        //开启占位符
        enablePlaceholders = true,
        //预刷新的距离，距离最后一个 item 多远时加载数据
        prefetchDistance = 4,
        //初始化加载数量，默认为 pageSize * 3
        initialLoadSize = 20
    )


}