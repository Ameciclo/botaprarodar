package com.brunotmgomes.ui.extensions

import android.content.Context
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun snackBarMaker(
    message: String,
    container: View,
    timeType: Int = Snackbar.LENGTH_SHORT,
    actionButtonText: String = "",
    actionMethod: () -> Unit = {}
) =
    Snackbar.make(
        container,
        message,
        timeType
    ).apply {
        if (actionButtonText.isNotEmpty()) {
            setAction(
                actionButtonText
            ) { actionMethod }
        }
    }


fun Context.showDialogMessage(
    title: String = "",
    message: String,
    isConfirmation: Boolean = false,
    positiveButtonText: String = "",
    positiveMethod: () -> Unit = {}
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