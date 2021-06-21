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
        return try {
            val request = chain.request()
            if (isNull(request)) {
                LogUtils.e("request is null")
                Response.Builder().build()
            }
            val httpMethod = request.method()
            val httpUrl = request.url()
            val httpHeader = request.headers()
            print("METHOD:$httpMethod\n URL:$httpUrl\n HEAD:$httpHeader")
            val response = chain.proceed(request)
            if (isNull(response)) {
                print("LogInterceptor's response is null")
            }
            val type = response.body()!!.contentType()
            val content = response.body()!!.string()
            print("请求响应：\n$content")
            response.newBuilder().body(ResponseBody.create(type, content)).build()
        } catch (e: Exception) {
            LogUtils.e(e.message)
            null
        }
    }

}

