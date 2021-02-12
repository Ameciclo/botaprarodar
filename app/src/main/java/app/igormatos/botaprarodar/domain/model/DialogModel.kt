package app.igormatos.botaprarodar.domain.model

import android.view.View
import java.io.Serializable

class DialogModel(
    val icon: Int? = null,
    val title: String? = null,
    val message: String? = null,
    val titlePrimaryButton: String,
    val titleSecondaryButton: String? = null,
    val primaryListenerButton: View.OnClickListener,
    val secondListenerButton: View.OnClickListener? = null
) : Serializable