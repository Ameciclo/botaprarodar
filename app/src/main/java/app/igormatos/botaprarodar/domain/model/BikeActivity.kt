package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BikeActivity(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("serialNumber")
    var serialNumber: String? = "",
    @SerializedName("orderNumber")
    var orderNumber: Long? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("photoThumbnailPath")
    var photoThumbnailPath: String? = "",
    @SerializedName("date")
    var date: String? = "",
    @SerializedName("status")
    var status: String? = ""
) : Parcelable