package utils

import android.content.Context
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun snackBarMaker(message: String, container: View) {
    Snackbar.make(
        container,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Context.showDialogMessage(
    title: String = "",
    message: String,
    isConfirmation: Boolean = false,
    positiveButtonText: String = "",
    positiveMethod: () -> Unit
) {
    MaterialAlertDialogBuilder(this).apply {
        setTitle(title)
        setMessage(message)
        if (isConfirmation) {
            setPositiveButton(positiveButtonText) { _, _ ->
                positiveMethod()
            }

        }
    }.show()
}