package app.igormatos.botaprarodar.domain.model.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    var id: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("gender")
    var gender: Int? = 3,
    @SerializedName("profile_picture")
    var profilePicture: String? = "",
    @SerializedName("residence_proof_picture")
    var residenceProofPicture: String? = "",
    @SerializedName("doc_picture")
    var docPicture: String? = "",
    @SerializedName("doc_picture_back")
    var docPictureBack: String? = "",
    @SerializedName("doc_type")
    var docType: Int? = 0,
    @SerializedName("doc_number")
    var docNumber: Long? = 0L,
    @SerializedName("profile_picture_thumbnail")
    var profilePictureThumbnail: String? = ""
)