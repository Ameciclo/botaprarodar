package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quiz(
    @SerializedName("destination")
    var destination: String? = "",
    @SerializedName("reason")
    var reason: Int? = null,
    @SerializedName("problemsDuringRiding")
    var problemsDuringRiding: String? = "",
    @SerializedName("giveRide")
    var giveRide: String? = ""
) : Parcelable
