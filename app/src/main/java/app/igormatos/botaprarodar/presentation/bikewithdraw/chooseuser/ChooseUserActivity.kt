package app.igormatos.botaprarodar.presentation.bikewithdraw.chooseuser

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Withdraw
import app.igormatos.botaprarodar.presentation.returnbicycle.WITHDRAWAL_EXTRA
import kotlinx.android.synthetic.main.activity_choose_user.*
import org.parceler.Parcels

class ChooseUserActivity : AppCompatActivity() {

    private var withdrawalParcelable: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        withdrawalParcelable =
            if (intent.hasExtra(WITHDRAWAL_EXTRA)) intent.getParcelableExtra(
                WITHDRAWAL_EXTRA
            ) else null

        val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
