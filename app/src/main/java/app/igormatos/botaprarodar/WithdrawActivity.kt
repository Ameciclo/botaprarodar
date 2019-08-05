package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestListener
import app.igormatos.botaprarodar.util.getSelectedCommunityId
import kotlinx.android.synthetic.main.activity_rent.*

class WithdrawActivity : AppCompatActivity() {

    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        logRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        itemAdapter = ItemAdapter(activity = this)
        logRecyclerView.adapter = itemAdapter

        FirebaseHelper.getBicycles(getSelectedCommunityId(), object : RequestListener<Bicycle> {
            override fun onChildChanged(result: Bicycle) {
                itemAdapter.updateItem(result)
            }

            override fun onChildAdded(result: Bicycle) {
                itemAdapter.addItem(result)
            }

            override fun onChildRemoved(result: Bicycle) {
                itemAdapter.removeItem(result)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}
