package com.example.room.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlin.random.Random

suspend fun Context.generateProfilePhoto(): Bitmap {
    val imageIndex = Random.nextInt(0, Images.IMAGES.size)
    val url = Images.IMAGES[imageIndex]
    val loading = ImageLoader(this)
    val request = ImageRequest.Builder(this)
        .data(url)
        .build()

    val result = (loading.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}
