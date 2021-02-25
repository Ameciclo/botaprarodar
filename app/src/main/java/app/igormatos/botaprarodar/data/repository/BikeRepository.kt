package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Item
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class BikeRepository(
    private val bicycleApi: BicycleApi,
    private val firebaseDatabase: FirebaseDatabase
) {

    lateinit var postListener: ValueEventListener

    suspend fun getBicycles(communityId: String): Map<String, Bike> {
        return withContext(Dispatchers.IO) {
            return@withContext bicycleApi.getBicycles(communityId = communityId).await()
        }
    }

    suspend fun addNewBike(communityId: String, bicycle: BicycleRequest): String {
        return bicycleApi.addNewBike(communityId, bicycle).name
    }

    suspend fun updateBike(communityId: String, bicycle: BicycleRequest): String {
        return bicycleApi.updateBike(communityId, bicycle.id, bicycle).name
    }

    suspend fun getBikes(communityId: String) = callbackFlow<SimpleResult<List<Bike>>> {

        postListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.sendBlocking(SimpleResult.Error(databaseError.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { data ->
                    verifyItemIdAndUpdate(data.getValue(Bike::class.java)!!, data, communityId)
                    data.getValue(Bike::class.java)
                }
                this@callbackFlow.sendBlocking(SimpleResult.Success(items.filterNotNull()))
            }
        }

        firebaseReference(communityId)
            .orderByChild(REFERENCE_AVAILABLE)
            .equalTo(true)
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase
                .getReference(REFERENCE_COMMUNITIES)
                .removeEventListener(postListener)
        }
    }

    private fun verifyItemIdAndUpdate(
        bike: Bike,
        snapshot: DataSnapshot,
        communityId: String
    ) {
        if (bike.id.isNullOrEmpty()) {
            snapshot.key?.let { key ->
                bike.id = key
                firebaseReference(communityId).child(key).setValue(bike)
            }
        }
    }

    private fun firebaseReference(communityId: String): DatabaseReference {
        return firebaseDatabase
            .getReference(REFERENCE_COMMUNITIES)
            .child(communityId)
            .child(REFERENCE_BICYCLES)
    }

    companion object {
        private const val REFERENCE_BICYCLES = "bicycles"
        private const val REFERENCE_COMMUNITIES = "communities"
        private const val REFERENCE_AVAILABLE = "available"
    }
}