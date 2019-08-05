package app.igormatos.botaprarodar.local.model

import app.igormatos.botaprarodar.network.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

interface Item {

    val path: String

    var id: String?

    fun title(): String

    fun subtitle(): String

    fun iconPath(): String

    fun removeRemote(): Boolean {
        val reference = FirebaseDatabase.getInstance().getReference("$path/$id")
        reference.removeValue()
        return true
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