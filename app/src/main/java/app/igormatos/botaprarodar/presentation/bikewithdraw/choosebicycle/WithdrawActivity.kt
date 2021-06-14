package app.igormatos.botaprarodar.presentation.bikewithdraw.choosebicycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.ItemAdapter
import kotlinx.android.synthetic.main.activity_rent.*
import org.koin.android.ext.android.inject

class WithdrawActivity : AppCompatActivity() {

    lateinit var itemAdapter: ItemAdapter

    private val preferencesModule: SharedPreferencesModule by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        logRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        itemAdapter =
            ItemAdapter(activity = this)
        logRecyclerView.adapter = itemAdapter

        val selectedCommunityId = preferencesModule.getJoinedCommunity().id!!
        FirebaseHelper.getBicycles(
            selectedCommunityId,
            true,
            object : RequestListener<Bike> {
                override fun onChildChanged(result: Bike) {
                    itemAdapter.updateItem(result)
                }

                override fun onChildAdded(result: Bike) {
                    itemAdapter.addItem(result)
                }

                override fun onChildRemoved(result: Bike) {
                    itemAdapter.removeItem(result)
                }

            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
