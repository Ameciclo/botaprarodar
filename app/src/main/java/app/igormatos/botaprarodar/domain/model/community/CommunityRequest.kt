package app.igormatos.botaprarodar.domain.model.community

import com.google.gson.annotations.SerializedName

data class CommunityRequest (
    @SerializedName("address")
    val address: String?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("org_email")
    val orgEmail: String?,
    @SerializedName("org_name")
    val orgName: String?
)