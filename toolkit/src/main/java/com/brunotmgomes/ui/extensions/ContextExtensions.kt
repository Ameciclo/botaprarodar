package com.brunotmgomes.ui.extensions

import android.content.Context
import android.os.Environment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun Context.showLoadingDialog(viewId: Int): androidx.appcompat.app.AlertDialog? {
    return MaterialAlertDialogBuilder(this)
        .setView(viewId)
        .setCancelable(false)
        .show()
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}