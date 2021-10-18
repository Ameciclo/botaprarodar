package com.brunotmgomes.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarModuleImpl : SnackbarModule {
    override fun make(view: View, text: CharSequence, duration: Int): Snackbar {
        return Snackbar.make(view, text, duration)
    }
}