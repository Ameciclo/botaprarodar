package app.igormatos.botaprarodar.domain.model.community

import com.google.gson.annotations.SerializedName

data class CommunityBody (
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("org_name")
    val orgName: String = "",
    @SerializedName("org_email")
    val orgEmail: String = "",
    @SerializedName("created_date")
    val createdDate: Long = 0
)