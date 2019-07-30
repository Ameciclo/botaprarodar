package app.igormatos.botaprarodar.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseHelper {

    companion object {
        val instance = FirebaseDatabase.getInstance()
        val adminsReference = instance.getReference("admins")
        val communitiesPreview = instance.getReference("communities_preview")

        fun isUserAdmin(uid: String, listener: RequestListener<Boolean>) {
            val childReference = adminsReference.child(uid)
            childReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listener.onCompleted(snapshot.value != null)
                }

                override fun onCancelled(p0: DatabaseError) {
                    listener.onError(RequestError.DEFAULT)
                }

            })
        }

        fun getCommunities(uid: String, listener: RequestListener<List<Community>>) {
            val childReference = adminsReference.child(uid)
            childReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isAdmin = snapshot.value != null

                    if (isAdmin) {
                        communitiesPreview.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                listener.onError(RequestError.DEFAULT)
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val communities = snapshot.children.mapNotNull { communitySnapshot ->
                                    communitySnapshot.getValue(Community::class.java)
                                }

                                listener.onCompleted(communities)
                            }

                        })
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    listener.onError(RequestError.DEFAULT)
                }

            })
        }
    }
}