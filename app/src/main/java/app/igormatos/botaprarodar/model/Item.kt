package app.igormatos.botaprarodar.model

import com.google.firebase.database.FirebaseDatabase

interface Item {

    val path : String

    var id : String?

    fun title(): String

    fun subtitle(): String

    fun iconPath(): String

    fun removeRemote() : Boolean {
        val reference = FirebaseDatabase.getInstance().getReference("$path/$id")
        reference.removeValue()
        return true
    }
}