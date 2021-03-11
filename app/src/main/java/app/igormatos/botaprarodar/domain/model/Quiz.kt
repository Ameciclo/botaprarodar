package app.igormatos.botaprarodar.domain.model

import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("destination")
    val destination: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("usage")
    val usage: String?
)