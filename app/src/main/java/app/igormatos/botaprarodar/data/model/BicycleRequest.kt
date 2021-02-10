package app.igormatos.botaprarodar.data.model

import com.google.gson.annotations.SerializedName

data class BicycleRequest(
    @SerializedName("id")
    var id: String,
    @SerializedName("available")
    var available: Boolean,
    @SerializedName("in_use")
    var inUse: Boolean,
    @SerializedName("name")
    var name: String,
    @SerializedName("order_number")
    var orderNumber: Long,
    @SerializedName("path")
    val path: String = "bike",
    @SerializedName("photo_path")
    val photoPath: String = "",
    @SerializedName("photo_thumbnail_path")
    val photoThumbnailPath: String= "",
    @SerializedName("serial_number")
    var serialNumber: String,
    @SerializedName("created_date")
    var createdDate: String
)