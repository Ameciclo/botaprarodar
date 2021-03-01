package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class UserRepository(
    private val userApi: UserApi,
    private val firebaseDatabase: FirebaseDatabase
) {

    suspend fun addNewUser(communityId: String, user: UserRequest): String {
        return userApi.addUser(communityId, user).name
    }

    suspend fun updateUser(communityId: String, user: UserRequest): String {
        return userApi.updateUser(communityId, user.id, user).name
    }

    suspend fun getUsers(communityId: String) = callbackFlow<SimpleResult<List<User>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.sendBlocking(SimpleResult.Error(databaseError.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { data ->
                    verifyItemIdAndUpdate(data.getValue(User::class.java), data, communityId)
                    data.getValue(User::class.java)
                }
                this@callbackFlow.sendBlocking(SimpleResult.Success(items.filterNotNull()))
            }
        }

        firebaseDatabase
            .getReference(REFERENCE_COMMUNITIES)
            .child(communityId)
            .child(REFERENCE_USERS)
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
        user: User?,
        snapshot: DataSnapshot,
        communityId: String
    ) {
        if (user?.id.isNullOrEmpty()) {
            snapshot.key?.let { key ->
                user?.id = key
                firebaseDatabase.getReference(REFERENCE_COMMUNITIES).child(communityId)
                    .child(REFERENCE_USERS).child(key).setValue(user)
            }
        }
    }

    companion object {
        private const val REFERENCE_USERS = "users"
        private const val REFERENCE_COMMUNITIES = "communities"
        private const val REFERENCE_AVAILABLE = "available"
    }
}