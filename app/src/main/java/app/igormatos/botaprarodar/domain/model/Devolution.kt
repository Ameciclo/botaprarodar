package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Devolution (
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("quiz")
    val quiz: Quiz? = null
) : Parcelable