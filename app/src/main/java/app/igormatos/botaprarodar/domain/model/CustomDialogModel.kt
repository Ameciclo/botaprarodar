package app.igormatos.botaprarodar.domain.model

import android.view.View
import java.io.Serializable

class CustomDialogModel(
    val icon: Int? = null,
    val title: String? = null,
    val message: String? = null,
    val primaryButtonText: String,
    val secondaryButtonText: String? = null,
    val primaryButtonListener: View.OnClickListener,
    val secondaryButtonListener: View.OnClickListener? = null
) : Serializable