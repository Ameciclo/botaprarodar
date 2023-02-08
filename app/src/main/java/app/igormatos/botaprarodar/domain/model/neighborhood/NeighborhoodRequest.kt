package app.igormatos.botaprarodar.domain.model.neighborhood

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.util.*

@Parcel
data class NeighborhoodRequest(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = ""
)