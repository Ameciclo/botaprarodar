package app.igormatos.botaprarodar.common.util

import android.content.Context
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.local.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun Context.showLoadingDialog(): AlertDialog {
    return MaterialAlertDialogBuilder(this)
        .setView(R.layout.loading_dialog_animation)
        .setCancelable(false)
        .show()
}

fun Context.getSelectedCommunityId(): String {
    return Preferences.getJoinedCommunity(this).id!!
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}