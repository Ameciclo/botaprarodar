package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Item
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_list.*

class BicyclesFragment : androidx.fragment.app.Fragment() {

    private val bicyclesFragment = FirebaseDatabase.getInstance().getReference("bicycles")
    lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemAdapter = ItemAdapter(activity = activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, AddBikeActivity::class.java)
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
                val user = p0.getValue(Bicycle::class.java)
                itemAdapter.updateItem(user as Item)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val bicycle = p0.getValue(Bicycle::class.java)
                itemAdapter.addItem(bicycle as Item)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val bicycle = p0.getValue(Bicycle::class.java)
                itemAdapter.removeItem(bicycle as Item)
            }
        }

        bicyclesFragment.addChildEventListener(bicyclesListener)
    }
}