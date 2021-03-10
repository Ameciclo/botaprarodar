package app.igormatos.botaprarodar.domain.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcel
data class User(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("createdDate")
    var createdDate: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("gender")
    var gender: Int = 3,
    @SerializedName("profilePicture")
    var profilePicture: String? = null,
    @SerializedName("residenceProofPicture")
    var residenceProofPicture: String? = null,
    @SerializedName("docPicture")
    var docPicture: String? = null,
    @SerializedName("docPictureBack")
    var docPictureBack: String? = null,
    @SerializedName("docType")
    var docType: Int = 0,
    @SerializedName("docNumber")
    var docNumber: Long = 0,
    @SerializedName("profilePictureThumbnail")
    var profilePictureThumbnail: String? = null,
    @SerializedName("communityId")
    var communityId: String? = ""
) : Item {

    @SerializedName("path")
    override var path: String = "users"
    @SerializedName("id")
    override var id: String? = null
    @SerializedName("available")
    override var isAvailable: Boolean = true

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        createdDate = dateFormat.format(date)
    }

    override fun title(): String {
        val nameCapitalized =
            name?.split(" ")?.take(2)?.map { it.capitalize() }?.joinToString(separator = " ") { it }

        return nameCapitalized ?: "erro_01"
    }

    override fun iconPath(): String {
        return profilePictureThumbnail ?: profilePicture
        ?: "https://api.adorable.io/avatars/135/abott@adorable.png"
    }

    override fun subtitle(): String {
        return "Cadastrado desde $createdDate"
    }

}