package com.brunotmgomes.ui.extensions

fun Long?.transformNullToEmpty(): Long {
    return this?.let {
        this
    } ?: 0L
}