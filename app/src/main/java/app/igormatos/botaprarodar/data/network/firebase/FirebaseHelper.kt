package app.igormatos.botaprarodar.data.network.firebase

import android.net.Uri
import android.os.Handler
import app.igormatos.botaprarodar.data.network.RequestError
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.SingleRequestListener
import app.igormatos.botaprarodar.domain.model.*
import app.igormatos.botaprarodar.domain.model.community.Community
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

@Deprecated("use FirebaseHelperModule instead")
object FirebaseHelper {

    val instance = FirebaseDatabase.getInstance()

    val adminsReference = instance.getReference("admins")
    val communities = instance.getReference("communities")

    private var communityId: String? = null

    fun setCommunityId(communityId: String) {
        FirebaseHelper.communityId = communityId
    }

    fun addCommunity(community: Community, listener: SingleRequestListener<Boolean>) {
        val communityKey = communities.push().key!!
        community.id = communityKey

        listener.onStart()
        communities.child(communityKey).setValue(community).addOnSuccessListener {
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
                    communities.addListenerForSingleValueEvent(object : ValueEventListener {
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
                    communities.addListenerForSingleValueEvent(object : ValueEventListener {
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

    fun getUsers(
        communityId: String,
        onlyAvailable: Boolean? = false,
        listener: RequestListener<Item>
    ) {
        val usersReference = communities.child(communityId).child("users")

        val usersListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
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

        onlyAvailable?.let {
            if (it) {
                usersReference.orderByChild("available").equalTo(true)
                    .addChildEventListener(usersListener)
            } else {
                usersReference.addChildEventListener(usersListener)
            }
        }

    }

    fun getWithdrawals(
        communityId: String,
        error: () -> Unit,
        listener: RequestListener<Withdraw>
    ) {
        val withdrawalsReference = communities.child(communityId).child("withdrawals")
        val withdrawalsListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                error()
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

        withdrawalsReference.orderByChild("modified_time")
            .addChildEventListener(withdrawalsListener)
    }

    fun saveItem(item: Item, block: (Boolean) -> Unit) {
        if (communityId == null) block(false)

        val reference = communities.child(communityId!!).child(item.path)
        val key = item.id ?: reference.push().key!!
        item.id = key

        reference.child(key).setValue(item).addOnSuccessListener {
            block(true)
        }.addOnFailureListener {
            block(false)
        }
    }

    fun updateBicycleStatus(id: String, inUse: Boolean, block: (Boolean) -> Unit) {
        if (communityId == null) {
            block(false)
            return
        }

        val bicycleReference = communities.child(communityId!!).child("bicycles").child(id)

        bicycleReference.child("in_use").setValue(inUse).addOnSuccessListener {
            block(true)
        }.addOnFailureListener {
            block(false)
        }

    }

    fun getWithdrawalFromBicycle(bicycleId: String, block: (Withdraw?) -> Unit) {
        if (communityId == null) {
            block(null)
            return
        }

        val withdrawalsQuery =
            communities.child(
                communityId!!
            ).child("withdrawals").orderByChild("bicycle_id").equalTo(bicycleId).limitToLast(1)
        withdrawalsQuery.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                block(null)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                block(null)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                block(null)
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                snapshotToWithdraw(snapshot)?.let {
                    if (it.isRent()) {
                        block(it)
                    } else {
                        block(null)
                    }

                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                block(null)
            }

        })


    }

    fun getBicycles(
        communityId: String,
        onlyAvailable: Boolean? = false,
        listener: RequestListener<Bike>
    ) {
        val bicyclesReference = communities.child(communityId).child("bicycles")

        val bicyclesListener = object : ChildEventListener {
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

        onlyAvailable?.let {
            if (it) {
                bicyclesReference.orderByChild("available").equalTo(true)
                    .addChildEventListener(bicyclesListener)
            } else {
                bicyclesReference.addChildEventListener(bicyclesListener)
            }
        }
    }


    fun uploadImage(imagePath: String, block: (Boolean, String?, String?) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val fileReference = storageRef.child("$ts.jpg")
        val thumbReference = storageRef.child("thumb_$ts.jpg")

        val file = Uri.fromFile(File(imagePath))
        val uploadTask = fileReference.putFile(file)

        uploadTask
            .addOnFailureListener {
                block(false, null, null)
            }
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { fullImagePath ->

                    Handler().postDelayed({
                        thumbReference.downloadUrl.addOnSuccessListener { thumbPath ->
                            block(true, fullImagePath.toString(), thumbPath.toString())
                        }.addOnFailureListener {
                            block(true, fullImagePath.toString(), null)
                        }
                    }, 4000) // Workaround - wait the time to Firebase Function make the thumbnail
                }
            }

    }


    private fun snapshotToWithdraw(snapshot: DataSnapshot): Withdraw? {
        return snapshot.getValue(Withdraw::class.java)
    }

    private fun snapshotToWithUser(snapshot: DataSnapshot): User? {
        return snapshot.getValue(User::class.java)
    }

    private fun snapshotToBicycle(snapshot: DataSnapshot): Bike? {
        return snapshot.getValue(Bike::class.java)
    }

}