package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class UserRepository(
    private val userApi: UserApi,
    private val firebaseDatabase: FirebaseDatabase
) {

    suspend fun addNewUser(user: User): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userApi.addUser(user)
            }
        }
    }

    suspend fun updateUser(user: User): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userApi.updateUser(user.id.orEmpty(), user)
            }
        }
    }

    suspend fun getUsers(communityId: String) = callbackFlow<SimpleResult<List<User>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.sendBlocking(SimpleResult.Error(databaseError.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { data ->
                    verifyItemIdAndUpdate(data.getValue(User::class.java), data)
                    data.getValue(User::class.java)
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
                .getReference(REFERENCE_USERS)
                .removeEventListener(postListener)
        }
    }

    private fun verifyItemIdAndUpdate(
        user: User?,
        snapshot: DataSnapshot
    ) {
        if (user?.id.isNullOrEmpty()) {
            snapshot.key?.let { key ->
                user?.id = key
                firebaseReference().child(key).setValue(user)
            }
        }
    }

    private fun firebaseReference(): DatabaseReference {
        return firebaseDatabase
            .getReference(REFERENCE_USERS)
    }

    companion object {
        private const val REFERENCE_USERS = "users"
        private const val REFERENCE_AVAILABLE = "available"
    }
}