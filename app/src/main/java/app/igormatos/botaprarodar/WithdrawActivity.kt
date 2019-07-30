package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.local.model.Withdraw
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_rent.*

class WithdrawActivity : AppCompatActivity() {

    private val bicyclesReference = FirebaseDatabase.getInstance().getReference("bicycles")
    private val withdrawalsReference = FirebaseDatabase.getInstance().getReference("withdrawals")
    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        logRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        withdrawalsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val withdrawals = snapshot.children.map { it.getValue(Withdraw::class.java)!! }
                itemAdapter = ItemAdapter(withdrawals, this@WithdrawActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("result", "teste")
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}
