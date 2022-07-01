package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class User(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("createdDate")
    var createdDate: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("profilePicture")
    var profilePicture: String? = null,
    @SerializedName("docType")
    var docType: Int = 0,
    @SerializedName("docNumber")
    var docNumber: Long = 0,
    @SerializedName("profilePictureThumbnail")
    var profilePictureThumbnail: String? = null,
    @SerializedName("communityId")
    var communityId: String? = "",
    @SerializedName("racial")
    var racial: String? = null,
    @SerializedName("schooling")
    var schooling: String? = null,
    @SerializedName("schoolingStatus")
    var schoolingStatus: String? = null,
    @SerializedName("income")
    var income: String? = null,
    @SerializedName("age")
    var birthday: String? = null,
    @SerializedName("telephone")
    var telephone: String? = null,
    @SerializedName("userQuiz")
    var userQuiz: UserQuiz? = null,
    @SerializedName("path")
    override var path: String = "users",
    @SerializedName("id")
    override var id: String? = null,
    @SerializedName("available")
    override var isAvailable: Boolean = true,
    @SerializedName("isBlocked")
    var isBlocked: Boolean = false,
    @SerializedName("hasActiveWithdraw")
    var hasActiveWithdraw: Boolean = false
) : Parcelable, Item {

    init {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        createdDate = dateFormat.format(date)
    }

    override fun title(): String {
        val names = name?.split(" ")
        return names?.first()
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .orEmpty() + " " + names?.last()
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .orEmpty()
    }

    override fun iconPath(): String {
        return profilePictureThumbnail ?: profilePicture.orEmpty()
    }

    override fun subtitle(): String {
        return "Cadastrado desde $createdDate"
    }

    fun telephoneHide4Chars(): String {
        return telephone?.let {
            if (it.length >= 6) {
                val firstTwoChars = it.substring(0, 2)
                val lastFourChars = it.replace("-", "").takeLast(4)
                val phoneHided = "$firstTwoChars •••• $lastFourChars"

                "Telefone: $phoneHided"
            } else telephone!!
        }.orEmpty()
    }
}