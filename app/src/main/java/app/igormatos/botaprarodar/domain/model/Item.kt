package app.igormatos.botaprarodar.domain.model

import app.igormatos.botaprarodar.data.network.FirebaseHelper
import java.text.SimpleDateFormat
import java.util.*

interface Item {

    val path: String

    var id: String?

    var isAvailable: Boolean

    fun title(): String

    fun subtitle(): String

    fun iconPath(): String

    fun toggleAvailability(block: (Boolean) -> Unit) {
        isAvailable = !isAvailable
        saveRemote { block(it) }
    }

    fun saveRemote(block: (Boolean) -> Unit) {
        FirebaseHelper.saveItem(this, block)
    }

    fun getReadableDate(timestamp: Long): String {
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val inputDate = Date(timestamp)
        return outputFormat.format(inputDate)
    }
}