package app.igormatos.botaprarodar.domain.model

import com.google.gson.annotations.SerializedName

data class Devolution(
    @SerializedName("id")
    val id: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("quiz")
    val quiz: Quiz?
)