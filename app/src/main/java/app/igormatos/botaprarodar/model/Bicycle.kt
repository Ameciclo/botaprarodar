package app.igormatos.botaprarodar.model

import java.text.SimpleDateFormat
import java.util.*

class Bicycle : Item {

    override val path: String = "bicycles"

    override var id: String? = null
    var name: String? = null
    var serial_number: String? = null
    var order_number: Long? = null
    var photo_path: String? = null
    var created_date: String? = null

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        created_date = dateFormat.format(date)
    }


    override fun title(): String {
        return name ?: "Erro - nome"
    }

    override fun subtitle(): String {
        return "Cadastrado desde $created_date"
    }

    override fun iconPath(): String {
        return photo_path ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }
}