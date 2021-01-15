package app.igormatos.botaprarodar.domain.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.firebase.database.IgnoreExtraProperties
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
class Bike: Item {

    override val path: String = "bicycles"

    override var id: String? = null

    override var isAvailable: Boolean = true

    var name: String? = null

    var serialNumber: String? = null

    var orderNumber: Long? = null

    var photoPath: String? = null

    var createdDate: String? = null

    var photoThumbnailPath: String? = null

    @field:JvmField
    var inUse: Boolean = false

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