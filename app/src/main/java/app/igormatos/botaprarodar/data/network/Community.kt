package app.igormatos.botaprarodar.data.network

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Community(
    var name: String? = "",
    var description: String? = "",
    var address: String? = "",
    var org_name: String? = "",
    var org_email: String? = "",
    var id: String? = ""

) {
    var created_date: Long?


    init {
        val date = Calendar.getInstance().time
        created_date = date.time
    }
}