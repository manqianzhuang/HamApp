package com.mm.hamcompose.ui.page.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {

    fun <T> flowable(data: T): Flow<T> = flow { emit(data) }.flowOn(Dispatchers.IO)

}