package app.igormatos.botaprarodar.domain.model.admin

import com.google.gson.annotations.SerializedName

data class AdminRequest(
    @SerializedName("uid")
    val uid: String?,
    @SerializedName("email")
    val email: String?,
)
