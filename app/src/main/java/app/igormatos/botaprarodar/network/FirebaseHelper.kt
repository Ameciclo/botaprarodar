package app.igormatos.botaprarodar.network

import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.local.model.User
import app.igormatos.botaprarodar.local.model.Withdraw
import com.google.firebase.database.*

object FirebaseHelper {

    val instance = FirebaseDatabase.getInstance()

    val adminsReference = instance.getReference("admins")
    val communitiesPreview = instance.getReference("communities_preview")
    val communities = instance.getReference("communities")

    private var communityId: String? = null

    fun setCommunityId(communityId: String) {
        this.communityId = communityId
    }

    fun addCommunity(community: Community, listener: SingleRequestListener<Boolean>) {
        val communityKey = communitiesPreview.push().key!!
        community.id = communityKey

        listener.onStart()
        communitiesPreview.child(communityKey).setValue(community).addOnSuccessListener {
            listener.onCompleted(true)
        }.addOnFailureListener {
            listener.onError(RequestError.DEFAULT)
        }

    }

    fun getCommunities(
        uid: String,
        email: String,
        listener: SingleRequestListener<Pair<Boolean, List<Community>>>
    ) {

        listener.onStart()
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

                            listener.onCompleted(Pair(true, communities))
                        }

                    })
                } else {
                    communitiesPreview.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            listener.onError(RequestError.DEFAULT)
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val communities = snapshot.children.mapNotNull { communitySnapshot ->
                                communitySnapshot.getValue(Community::class.java)
                            }.filter {
                                it.org_email == email
                            }

                            listener.onCompleted(Pair(false, communities))
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

    fun saveItem(item: Item, block: (Boolean) -> Unit) {
        if (communityId == null) block(false)

        val reference = communities.child(communityId!!).child(item.path)
        val key = reference.push().key!!
        item.id = key

        reference.child(key).setValue(item).addOnSuccessListener {
            block(true)
        }.addOnFailureListener {
            block(false)
        }
    }

    fun updateItem(item: Item, block: (Boolean) -> Unit) {
        if (communityId == null || item.id == null) block(false)

        val reference = communities.child(communityId!!).child(item.path).child(item.id!!)

        reference.setValue(item).addOnSuccessListener {
            block(true)
        }.addOnFailureListener {
            block(false)
        }
    }

    fun updateBicycleStatus(id: String, isAvailable: Boolean, block: (Boolean) -> Unit) {
        if (communityId == null) block(false)

        val bicycleReference = communities.child(communityId!!).child("bicycles").child(id)

        bicycleReference.child("is_available").setValue(isAvailable).addOnSuccessListener {
            block(true)
        }.addOnFailureListener {
            block(false)
        }
    }

    fun getBicycles(communityId: String, listener: RequestListener<Bicycle>) {
        val usersReference = communities.child(communityId).child("bicycles")

        val usersListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                snapshotToBicycle(p0)?.let {
                    listener.onChildChanged(it)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                snapshotToBicycle(p0)?.let {
                    listener.onChildAdded(it)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                snapshotToBicycle(p0)?.let {
                    listener.onChildRemoved(it)
                }
            }
        }

        usersReference.addChildEventListener(usersListener)
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


//    }
}