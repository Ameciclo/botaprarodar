package app.igormatos.botaprarodar.data.model

import android.os.Parcelable
import app.igormatos.botaprarodar.common.components.DialogListener
import kotlinx.android.parcel.Parcelize

@Parcelize
class DialogModel(
    val icon: Int? = null,
    val title: String? = null,
    val message: String? = null,
    val titlePrimaryButton: String,
    val titleSecondaryButton: String? = null,
    val listener: DialogListener
) : Parcelable