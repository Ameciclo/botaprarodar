package app.igormatos.botaprarodar.domain.model

import com.google.gson.annotations.SerializedName

data class AddDataResponse (
    @SerializedName("name")
    val name: String
)