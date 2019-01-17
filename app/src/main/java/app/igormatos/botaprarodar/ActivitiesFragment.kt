package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.igormatos.botaprarodar.model.Bicycle
import app.igormatos.botaprarodar.model.Item
import app.igormatos.botaprarodar.model.Withdraw
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_list.*


class ActivitiesFragment : Fragment() {

    private val withdrawalsReference = FirebaseDatabase.getInstance().getReference("withdrawals")
    val itemAdapter = ItemAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        val bitmap = ContextCompat.getDrawable(context!!, R.drawable.ic_directions_bike)
        rootView.addItemFab.setImageDrawable(bitmap)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener { it ->
            val intent = Intent(it.context, WithdrawActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter

        val bicyclesListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
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

        withdrawalsReference.addChildEventListener(bicyclesListener)
    }

}