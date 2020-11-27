package utils

import androidx.appcompat.app.AlertDialog
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.createLoading(viewId: Int) : AlertDialog {
    return MaterialAlertDialogBuilder(this)
        .setView(viewId)
        .setCancelable(false)
        .create()
}