package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Withdraws(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("user")
    val user: User? = null
) : Parcelable