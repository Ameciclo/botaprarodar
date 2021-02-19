package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class BikeRepository(
    private val bicycleApi: BicycleApi,
    private val firebaseDatabase: FirebaseDatabase
) {

    suspend fun getBicycles(communityId: String): Map<String, Bike> {
        return withContext(Dispatchers.IO) {
            return@withContext bicycleApi.getBicycles(communityId = communityId).await()
        }
    }

    suspend fun addNewBike(communityId: String, bicycle: BicycleRequest): String {
        return bicycleApi.addNewBicycle(communityId, bicycle).name
    }

    @ExperimentalCoroutinesApi
    suspend fun getBikes(communityId: String) = callbackFlow<List<Bike>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                this@callbackFlow.sendBlocking()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { data ->
                    data.getValue(Bike::class.java)
                }
                this@callbackFlow.sendBlocking(items.filterNotNull())
            }
        }

        firebaseDatabase
            .getReference(REFERENCE_COMMUNITIES)
            .child(communityId)
            .child(REFERENCE_BICYCLES)
            .orderByChild(REFERENCE_AVAILABLE)
            .equalTo(true)
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase
                .getReference(REFERENCE_COMMUNITIES)
                .removeEventListener(postListener)
        }
    }

    companion object {
        private const val REFERENCE_BICYCLES = "bicycles"
        private const val REFERENCE_COMMUNITIES = "communities"
        private const val REFERENCE_AVAILABLE = "available"
    }
}