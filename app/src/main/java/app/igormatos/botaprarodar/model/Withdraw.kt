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
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
//        dateFormat.format(date)
        created_date = date.time
        modified_time = date.time
    }

    fun isRent(): Boolean {
        return returned_date == null
    }

    override fun title(): String {
        return bicycle_name ?: "_"
    }

    override fun subtitle(): String {
        return """"$user_name" em ${readableDate()}"""
    }

    private fun readableDate(): String {
        var timestamp = if (isRent()) created_date else returned_date
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val inputDate = Date(timestamp!!)
        return outputFormat.format(inputDate)
    }

    override fun iconPath(): String {
        return "https://api.adorable.io/avatars/135/abott@adorable.png"
    }

}