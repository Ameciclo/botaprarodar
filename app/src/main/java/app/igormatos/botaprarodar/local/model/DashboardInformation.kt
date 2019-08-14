package app.igormatos.botaprarodar.local.model

import com.google.gson.annotations.SerializedName

data class DashboardInformation(
    @SerializedName("trips_count") val tripsCount: Long,
    @SerializedName("current_month") val currentMonth: Month,
    @SerializedName("last_month") val lastMonth: Month,
    @SerializedName("male_count") val maleCount: Long,
    @SerializedName("female_count") val femaleCount: Long
)

data class Month(
    @SerializedName("trips_count") val tripsCount: Long,
    val name: String
)