package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class BikeRepository(
    private val bicycleApi: BicycleApi,
    private val firebaseDatabase: FirebaseDatabase
) {

    lateinit var postListener: ValueEventListener

    suspend fun getBicycles(communityId: String): SimpleResult<Map<String, Bike>> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.getBicycles(communityId = communityId).await()
            }
        }
    }

    suspend fun addNewBike(bike: Bike): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.addNewBike(bike)
            }
        }
    }

    suspend fun updateBike(bike: Bike): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.updateBike(bike.id.orEmpty(), bike)
            }
        }
    }

    suspend fun getBikes(communityId: String) = callbackFlow<SimpleResult<List<Bike>>> {

        postListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.sendBlocking(SimpleResult.Error(databaseError.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { data ->
                    verifyItemIdAndUpdate(data.getValue(Bike::class.java)!!, data)
                    data.getValue(Bike::class.java)
                }
                this@callbackFlow.sendBlocking(SimpleResult.Success(items.filterNotNull()))
            }
        }

        firebaseReference()
            .orderByChild(REFERENCE_AVAILABLE)
            .equalTo(true)
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase
                .getReference(REFERENCE_BICYCLES)
                .removeEventListener(postListener)
        }
    }

    private fun verifyItemIdAndUpdate(
        bike: Bike,
        snapshot: DataSnapshot
    ) {
        if (bike.id.isNullOrEmpty()) {
            snapshot.key?.let { key ->
                bike.id = key
                firebaseReference().child(key).setValue(bike)
            }
        }
    }

    private fun firebaseReference(): DatabaseReference {
        return firebaseDatabase
            .getReference(REFERENCE_BICYCLES)
    }

    companion object {
        private const val REFERENCE_BICYCLES = "bikes"
        private const val REFERENCE_AVAILABLE = "available"
    }
}