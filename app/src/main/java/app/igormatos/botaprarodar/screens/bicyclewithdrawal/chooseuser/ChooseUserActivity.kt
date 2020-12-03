package app.igormatos.botaprarodar.screens.bicyclewithdrawal.chooseuser

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Withdraw
import app.igormatos.botaprarodar.screens.main.UsersFragment
import app.igormatos.botaprarodar.screens.returnbicycle.WITHDRAWAL_EXTRA
import kotlinx.android.synthetic.main.activity_choose_user.*
import org.koin.core.component.KoinApiExtension
import org.parceler.Parcels

@KoinApiExtension
class ChooseUserActivity : AppCompatActivity() {

    var withdrawalParcelable: Parcelable? = null

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
        val fragment =
            UsersFragment.newInstance(
                withdrawal
            )

        supportFragmentManager.beginTransaction().add(R.id.chooseUserFragment, fragment, "1").commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return when (id) {
            R.id.action_search -> true
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
