package app.igormatos.botaprarodar.network

import app.igormatos.botaprarodar.data.model.Bicycle
import app.igormatos.botaprarodar.data.model.Item
import app.igormatos.botaprarodar.data.model.Withdraw
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.core.component.KoinApiExtension
import utils.SimpleResult

@KoinApiExtension
interface FirebaseHelperModule {

    val instance: FirebaseDatabase

    val adminsReference: DatabaseReference
    val communitiesPreview: DatabaseReference
    val communities: DatabaseReference

    fun setCommunityId(communityId: String)

    suspend fun addCommunity(community: Community) : SimpleResult<Boolean>

    fun getCommunities(
        uid: String,
        email: String,
        listener: SingleRequestListener<Pair<Boolean, List<Community>>>
    )

    fun getUsers(
        communityId: String,
        onlyAvailable: Boolean? = false,
        listener: RequestListener<Item>
    )

    fun getWithdrawals(
        communityId: String,
        error: () -> Unit,
        listener: RequestListener<Withdraw>
    )

    fun saveItem(item: Item, block: (Boolean) -> Unit)

    fun updateBicycleStatus(id: String, inUse: Boolean, block: (Boolean) -> Unit)

    fun getWithdrawalFromBicycle(bicycleId: String, block: (Withdraw?) -> Unit)

    fun getBicycles(
        communityId: String,
        onlyAvailable: Boolean? = false,
        listener: RequestListener<Bicycle>
    )

    fun uploadImage(imagePath: String, block: (Boolean, String?, String?) -> Unit)
}