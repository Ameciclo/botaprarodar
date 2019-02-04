package app.igormatos.botaprarodar.model

import com.google.firebase.database.IgnoreExtraProperties
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
class Withdraw : Item {

    override val path: String = "withdrawals"

    override var id: String? = null

    var user_id: String? = null
    var created_date: String?
    var returned_date: String? = null
    var user_name: String? = null
    var user_image_path: String? = null
    var bicycle_name: String? = null
    var bicycle_id: String? = null
    var bicycle_image_path: String? = null
    var user: User? = null

    // End of Trip questions
    var destination: String? = null
    var trip_reason: String? = null
    var bicycle_rating: String? = null

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }

    override fun title(): String {
        return if (returned_date.isNullOrEmpty()) "Bicicleta retirada" else "Bicicleta devolvida"
    }

    override fun subtitle(): String {
        return if (returned_date.isNullOrEmpty()) "$user_name retirou a bicicleta no dia $created_date" else "$user_name devolveu a bicicleta no dia $returned_date"
    }

    override fun iconPath(): String {
        return "https://api.adorable.io/avatars/135/abott@adorable.png"
    }

}