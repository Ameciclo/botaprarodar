package com.brunotmgomes.ui.extensions

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

fun Drawable?.setDrawableColor(color: Int){
    this?.let {
        val wrapDrawable = DrawableCompat.wrap(it)
        DrawableCompat.setTint(wrapDrawable, color)
    }
}