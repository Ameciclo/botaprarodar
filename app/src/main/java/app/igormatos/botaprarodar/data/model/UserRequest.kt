package app.igormatos.botaprarodar.data.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("available")
    var available: Boolean = true,
    @SerializedName("address")
    var address: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("profile_picture")
    var profilePicture: String,
    @SerializedName("residence_proof_picture")
    var residenceProofPicture: String,
    @SerializedName("doc_picture")
    var docPicture: String,
    @SerializedName("doc_picture_back")
    var docPictureBack: String,
    @SerializedName("doc_type")
    var docType: Int,
    @SerializedName("doc_number")
    var docNumber: Long,
    @SerializedName("profile_picture_thumbnail")
    var profilePictureThumbnail: String,
    @SerializedName("path")
    var path: String = "users",
    @SerializedName("id")
    var id: String
)