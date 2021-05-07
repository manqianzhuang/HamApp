package com.mm.hamcompose.ui.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import javax.annotation.Nullable


@SuppressLint("CheckResult", "RememberReturnType")
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    @Nullable url: String,
    desc: String = "",
    placeholder: Int = R.drawable.no_banner,
    scale: ContentScale = ContentScale.Crop
) {

    val imageBitmap = remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    //Glide框架bitmap无法转换成imageBitmap, 原因不详
//    Glide
//        .with(HamApp.CONTEXT)
//        .asBitmap()
//        .load(url)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .into(object : SimpleTarget<Bitmap>() {
//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                if (imageBitmap.value==null) {
//                    LogUtils.e("网络图片， ${resource.width}*${resource.height}")
//                    imageBitmap.value = resource.asImageBitmap()
//                }
//            }
//        })
    Picasso
        .get()
        .load(url)
        .into(object : Target {
            override fun onBitmapLoaded(resource: Bitmap?, from: Picasso.LoadedFrom?) {
                if (imageBitmap.value==null) {
                    //LogUtils.e("网络图片， ${resource?.width}*${resource?.height}")
                    imageBitmap.value = resource?.asImageBitmap()
                }
            }
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })

    if (imageBitmap.value!=null) {
        //LogUtils.e("加载图片， ${imageBitmap.value?.width}*${imageBitmap.value?.height}")
        Image(
            bitmap = imageBitmap.value!!,
            contentDescription = desc,
            modifier = modifier,
            contentScale = scale
        )
    } else {
        Image(
            painter = painterResource(placeholder),
            contentDescription = desc,
            modifier = modifier,
            contentScale = scale
        )
    }


}
