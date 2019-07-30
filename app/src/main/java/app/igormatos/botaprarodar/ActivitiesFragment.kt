package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.igormatos.botaprarodar.local.Preferences
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.local.model.Withdraw
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*


class ActivitiesFragment : androidx.fragment.app.Fragment() {

    lateinit var withdrawalsReference: DatabaseReference
    val itemAdapter = ItemAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        val bitmap = ContextCompat.getDrawable(context!!, R.drawable.ic_directions_bike)
        rootView.addItemFab.setImageDrawable(bitmap)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val joinedCommunityId = Preferences.getJoinedCommunity(context!!).id!!
        withdrawalsReference =
            FirebaseDatabase.getInstance().getReference("communities/$joinedCommunityId").child("withdrawals")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, WithdrawActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerView.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )


        val bicyclesListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val withdraw = p0.getValue(Withdraw::class.java)
                itemAdapter.updateItem(withdraw as Item)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val withdraw = p0.getValue(Withdraw::class.java)
                itemAdapter.addItem(withdraw as Item)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val withdraw = p0.getValue(Withdraw::class.java)
                itemAdapter.removeItem(withdraw as Item)
            }
        }

        withdrawalsReference.orderByChild("modified_time").addChildEventListener(bicyclesListener)
    }

}