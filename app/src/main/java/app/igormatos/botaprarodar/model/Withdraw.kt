package app.igormatos.botaprarodar.model

import java.text.SimpleDateFormat
import java.util.*

class Withdraw (var user_id: String) : Item {

    override val path: String = "activities"

    override var id: String? = null

    var created_date: String?
    var return_date: String? = null

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }

    override fun title(): String {
        return if (return_date.isNullOrEmpty()) "Bicicleta retirada" else "Bicicleta devolvida"
    }

    override fun subtitle(): String {
        return "$user_id retirou a bicicleta no dia $created_date"
    }

    override fun iconPath(): String {
        return "https://api.adorable.io/avatars/135/abott@adorable.png"
    }
}