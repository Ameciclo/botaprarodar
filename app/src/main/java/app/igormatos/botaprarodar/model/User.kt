package app.igormatos.botaprarodar.model

import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
class User : Item {

    override val path: String = "users"

    override var id: String? = null
    var name: String? = null
    var created_date: String? = null
    var birthday: String? = null
    var address: String? = null
    var gender: String? = null
    var profile_picture: String? = null
    var residence_proof_picture: String? = null
    var doc_type: Int = 0
    var doc_number: Long = 0


    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }

    override fun title(): String {
        return name ?: "Title"
    }

    override fun iconPath(): String {
        return profile_picture ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }

    override fun subtitle(): String {
        return "Cadastrado desde $created_date"
    }

}