package app.igormatos.botaprarodar.model

import android.content.Context
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

    fun saveRemote(onSuccess: () -> Unit) {
        val reference = FirebaseDatabase.getInstance().getReference("$path")
        val key = reference.push().key!!
        id = key

        reference.child(key).setValue(this).addOnSuccessListener {
            onSuccess()
        }
    }
}