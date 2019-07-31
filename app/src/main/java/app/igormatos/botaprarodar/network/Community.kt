package app.igormatos.botaprarodar.network

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Community(
    val name: String? = "",
    val description: String? = "",
    val address: String? = "",
    val org_name: String? = "",
    val org_email: String? = "",
    var id: String? = ""
)