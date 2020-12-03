package app.igormatos.botaprarodar.data.model

import com.google.firebase.database.IgnoreExtraProperties
import org.koin.core.component.KoinApiExtension
import org.parceler.Parcel
import java.util.*

@KoinApiExtension
@IgnoreExtraProperties
@Parcel(Parcel.Serialization.BEAN)
class Withdraw : Item() {

    override val path: String = "withdrawals"

    override var id: String? = null

    override var isAvailable: Boolean = true

    var user_id: String? = null
    var created_date: Long?
    var returned_date: Long? = null
    var modified_time: Long?
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
        created_date = date.time
        modified_time = date.time
    }

    fun isRent(): Boolean {
        return returned_date == null
    }

    override fun title(): String {
        return bicycle_name?.capitalize() ?: "_"
    }

    override fun subtitle(): String {
        val timestamp = if (isRent()) created_date else returned_date
        val firstName = user_name?.split(" ")?.toList()?.first()?.capitalize() ?: "Usuário"

        return """$firstName em ${getReadableDate(timestamp!!)}"""
    }

    override fun iconPath(): String {
        return "https://api.adorable.i  o/avatars/135/abott@adorable.png"
    }

}