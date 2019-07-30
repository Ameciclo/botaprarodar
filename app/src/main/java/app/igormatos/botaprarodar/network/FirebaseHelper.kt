package app.igormatos.botaprarodar.network

import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.local.model.User
import app.igormatos.botaprarodar.local.model.Withdraw
import com.google.firebase.database.*

class FirebaseHelper {

    companion object {
        val instance = FirebaseDatabase.getInstance()

        val adminsReference = instance.getReference("admins")
        val communitiesPreview = instance.getReference("communities_preview")
        val communities = instance.getReference("communities")

        fun isUserAdmin(uid: String, listener: SingleRequestListener<Boolean>) {
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

        fun getCommunities(uid: String, listener: SingleRequestListener<List<Community>>) {
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

        fun getUsers(communityId: String, listener: RequestListener<Item>) {
            val usersReference = communities.child(communityId).child("users")

            val usersListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    //todo: refactor
                    val user = p0.getValue(User::class.java)
                    listener.onChildChanged(user as Item)
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java)
                    listener.onChildAdded(user as Item)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    listener.onChildRemoved(user as Item)
                }
            }

            usersReference.addChildEventListener(usersListener)
        }

        fun saveUser(communityId: String, user: User, listener: SingleRequestListener<Boolean>) {

        }

        fun getWithdrawals(communityId: String, listener: RequestListener<Item>) {
            val withdrawalsReference = communities.child(communityId).child("withdrawals")
            val withdrawalsListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    snapshotToWithdraw(p0)?.let {
                        listener.onChildChanged(it)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    snapshotToWithdraw(p0)?.let {
                        listener.onChildAdded(it)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    snapshotToWithdraw(p0)?.let {
                        listener.onChildRemoved(it)
                    }
                }
            }

            withdrawalsReference.orderByChild("modified_time").addChildEventListener(withdrawalsListener)
        }

        private fun snapshotToWithdraw(snapshot: DataSnapshot): Withdraw? {
            return snapshot.getValue(Withdraw::class.java)
        }

        private fun snapshotToWithUser(snapshot: DataSnapshot): User? {
            return snapshot.getValue(User::class.java)
        }

        private fun snapshotToBicycle(snapshot: DataSnapshot): Bicycle? {
            return snapshot.getValue(Bicycle::class.java)
        }


    }
}