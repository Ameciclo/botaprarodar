package app.igormatos.botaprarodar.presentation.login

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import com.brunotmgomes.ui.extensions.createLoading
import com.google.android.material.snackbar.Snackbar

abstract class BaseAuthActivity : AppCompatActivity() {

    abstract val snackBarview: View

    protected val loadingDialog: AlertDialog by lazy {
        createLoading(R.layout.loading_dialog_animation)
    }

    fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    protected fun showMessage(@StringRes messageId: Int) {
        makeSnackBar(messageId).show()
    }

    protected fun makeSnackBar(@StringRes messageId: Int) =
        Snackbar.make(
            snackBarview,
            messageId,
            Snackbar.LENGTH_SHORT
        )


    protected fun showSuccessConfirmDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        click: (() -> Unit)? = null

    ) {
        showConfirmDialog(
            R.drawable.ic_success,
            title,
            message,
            click
        )
    }

    protected fun showAlertConfirmDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        click: (() -> Unit)? = null

    ) {
        showConfirmDialog(
            R.drawable.ic_warning,
            title,
            message,
            click
        )
    }

    protected fun showAlertConfirmDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null

    ) {
        showConfirmDialog(
            R.drawable.ic_warning,
            title,
            message,
            onConfirm,
            onCancel
        )
    }

    private fun showConfirmDialog(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        @StringRes message: Int,
        click: (() -> Unit)? = null

    ) {
        showConfirmDialog(icon, title, message, click, null)
    }

    private fun showConfirmDialog(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        @StringRes message: Int,
        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null

    ) {
        val dialogModel = CustomDialogModel(
            icon = icon,
            title = getString(title),
            message = getString(message),
            primaryButtonText = getString(R.string.ok),
            primaryButtonListener = {
                onConfirm?.invoke()
            },
            secondaryButtonText = if (onCancel != null) getString(R.string.cancel) else null,
            secondaryButtonListener = {
                onCancel?.invoke()
            }
        )

        CustomDialog.newInstance(dialogModel).apply {
            isCancelable = false
        }.show(supportFragmentManager, CustomDialog.TAG)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.cancel()
    }
}