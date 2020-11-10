package com.brunotmgomes.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

interface SnackbarModule {

    fun make(view: View, text: CharSequence, duration: Int): Snackbar
}