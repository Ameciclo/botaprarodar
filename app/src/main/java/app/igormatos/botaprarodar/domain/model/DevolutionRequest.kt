package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DevolutionRequest(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("inUse")
    val inUse: Boolean = false,
    @SerializedName("devolutions")
    val devolutions: Map<String?, Devolution?>?
) : Parcelable