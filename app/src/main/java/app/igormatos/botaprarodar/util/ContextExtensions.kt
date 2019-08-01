package app.igormatos.botaprarodar.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.local.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Context.showLoadingDialog(): AlertDialog {

    return MaterialAlertDialogBuilder(this)
        .setView(R.layout.loading_dialog_animation)
        .setCancelable(false)
        .show()
}

fun Context.getSelectedCommunityId(): String {
    return Preferences.getJoinedCommunity(this).id!!
}