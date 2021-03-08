package app.igormatos.botaprarodar.domain.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
data class Bike(
    @SerializedName("name")
    var name: String? = null,
    var serial_number: String? = null,
    var order_number: Long? = null,
    var photo_path: String? = null,
    var created_date: String? = null,
    var photo_thumbnail_path: String? = null
) : Item {

    override var path: String = "bike"

    override var id: String? = null

    override var isAvailable: Boolean = true

    @field:JvmField
    var inUse: Boolean = false

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }

    override fun title(): String {
        return name ?: "Erro - nome"
    }

    override fun subtitle(): String {
        return "Ordem: $order_number | Série: $serial_number"
    }

    override fun iconPath(): String {
        return photo_thumbnail_path ?: photo_path
        ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }
}