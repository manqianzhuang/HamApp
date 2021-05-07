package com.mm.hamcompose.http

import android.os.Build
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.util.Objects.isNull
import javax.inject.Inject
import kotlin.jvm.Throws

/**
 * Created by Superman on 2021/1/29.
 */
class LogInterceptor @Inject constructor() : Interceptor {

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        if (isNull(request)) {
            LogUtils.e("request is null")
            Response.Builder().build()
        }
        LogUtils.w(
                "请求方式:${request.method()} \n" +
                "URL:${request.url()} \n" +
                "请求头部:${GsonUtils.toJson(request.headers())}")
        val response = chain.proceed(request)
        if (isNull(response))
            LogUtils.e("LogInterceptor's response is null")
        val type = response.body()!!.contentType()
        val content = response.body()!!.string()
        LogUtils.i("请求响应：\n$content")
        return response.newBuilder().body(ResponseBody.create(type, content)).build()
    }

}

