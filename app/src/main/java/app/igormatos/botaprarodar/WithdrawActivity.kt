package app.igormatos.botaprarodar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import app.igormatos.botaprarodar.model.Bicycle
import app.igormatos.botaprarodar.model.Item
import app.igormatos.botaprarodar.model.Withdraw
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_rent.*
import kotlinx.android.synthetic.main.fragment_list.*


class WithdrawActivity : AppCompatActivity() {

    private val bicyclesReference = FirebaseDatabase.getInstance().getReference("bicycles")
    private val withdrawalsReference = FirebaseDatabase.getInstance().getReference("withdrawals")
    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        logRecyclerView.layoutManager = LinearLayoutManager(this)

        withdrawalsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val withdrawals = snapshot.children.map { it.getValue(Withdraw::class.java)!! }
                itemAdapter = ItemAdapter(withdrawals)
                logRecyclerView.adapter = itemAdapter

                val bicyclesListener = object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val withdraw = p0.getValue(Bicycle::class.java)
                        itemAdapter.addItem(withdraw as Item)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        val withdraw = p0.getValue(Bicycle::class.java)
                        itemAdapter.removeItem(withdraw as Item)
                    }
                }

                bicyclesReference.addChildEventListener(bicyclesListener)
            }

        })



    }
}
