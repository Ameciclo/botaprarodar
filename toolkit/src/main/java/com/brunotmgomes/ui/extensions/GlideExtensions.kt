package com.brunotmgomes.ui.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadPath(path: String) {
    Glide.with(context)
        .load(path)
        .apply(RequestOptions.fitCenterTransform())
        .into(this)
}

fun ImageView.loadPath(path: String, context: Context) {
    Glide.with(context)
        .load(path)
        .apply(RequestOptions.fitCenterTransform())
        .into(this)
}

fun ImageView.loadPathOnCircle(path: String) {
    Glide.with(context)
        .load(path)
        .apply(RequestOptions.fitCenterTransform())
        .apply(RequestOptions().circleCrop())
        .into(this)
}