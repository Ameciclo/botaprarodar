package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.igormatos.botaprarodar.model.Item
import app.igormatos.botaprarodar.model.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_list.*

class UsersFragment : Fragment() {

    private val usersReference = FirebaseDatabase.getInstance().getReference("users")
    val itemAdapter = ItemAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, AddUserActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter

        val usersListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                itemAdapter.addItem(user as Item)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                itemAdapter.removeItem(user as Item)
            }
        }

        usersReference.addChildEventListener(usersListener)
    }

}