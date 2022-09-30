package app.igormatos.botaprarodar.domain.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserQuiz(
    @SerializedName("alreadyUseBPR")
    var alreadyUseBPR: Boolean? = null,
    @SerializedName("alreadyUseBPROpenQuestion")
    var alreadyUseBPROpenQuestion: String? = null,
    @SerializedName("motivation")
    var motivation: String? = null,
    @SerializedName("motivationOpenQuestion")
    var motivationOpenQuestion: String? = null,
    @SerializedName("alreadyAccidentVictim")
    var alreadyAccidentVictim: Boolean? = null,
    @SerializedName("problemsOnWayOpenQuestion")
    var problemsOnWayOpenQuestion: String? = null,
    @SerializedName("timeOnWayOpenQuestion")
    var timeOnWayOpenQuestion: String? = null
) : Parcelable
