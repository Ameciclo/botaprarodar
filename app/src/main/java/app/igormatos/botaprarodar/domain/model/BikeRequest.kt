package app.igormatos.botaprarodar.domain.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
data class BikeRequest(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("serialNumber")
    var serialNumber: String? = "",
    @SerializedName("orderNumber")
    var orderNumber: Long? = null,
    @SerializedName("communityId")
    var communityId: String? = "",
    @SerializedName("path")
    override var path: String = "bikes",
    @SerializedName("id")
    override var id: String? = null,
    @SerializedName("available")
    override var isAvailable: Boolean = true,
    @SerializedName("createdDate")
    var createdDate: String? = "",
    @SerializedName("inUse")
    var inUse: Boolean = false,
    @SerializedName("photoPath")
    var photoPath: String? = "",
    @SerializedName("photoThumbnailPath")
    var photoThumbnailPath: String? = "",
    @SerializedName("withdraws")
    var withdraws: Map<String, Withdraws>? = null,
    @SerializedName("devolutions")
    var devolutions: Map<String, Devolution>? = null
) : Item {

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        createdDate = dateFormat.format(date)
    }

    override fun title(): String {
        return name ?: "Erro - nome"
    }

    override fun subtitle(): String {
        return "Ordem: $orderNumber | Série: $serialNumber"
    }

    override fun iconPath(): String {
        return photoThumbnailPath ?: photoPath
        ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }
}