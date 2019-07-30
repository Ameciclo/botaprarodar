package app.igormatos.botaprarodar.network

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Community(
    val description: String? = "",
    val name: String? = "",
    val org_name: String? = "",
    val org_email: String? = "",
    val id: String? = ""
)