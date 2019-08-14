package app.igormatos.botaprarodar.local.model

import com.google.firebase.database.IgnoreExtraProperties
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
open class User : Item {

    override var path: String = "users"

    override var id: String? = null

    override var isAvailable: Boolean = true

    var name: String? = null
    var created_date: String? = null
    var birthday: String? = null
    var address: String? = null
    var gender: Int = 3
    var profile_picture: String? = null
    var residence_proof_picture: String? = null
    var doc_picture: String? = null
    var doc_picture_back: String? = null
    var doc_type: Int = 0
    var doc_number: Long = 0
    var profile_picture_thumbnail: String? = null

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }

    override fun title(): String {
        val nameCapitalized = name?.split(" ")?.take(2)?.map { it.capitalize() }?.joinToString(separator = " ") { it }

        return nameCapitalized ?: "erro_01"
    }

    override fun iconPath(): String {
        return profile_picture_thumbnail ?: profile_picture ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }

    override fun subtitle(): String {
        return "Cadastrado desde $created_date"
    }

}