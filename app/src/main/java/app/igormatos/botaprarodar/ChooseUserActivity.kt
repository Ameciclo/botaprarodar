package app.igormatos.botaprarodar

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import app.igormatos.botaprarodar.model.Withdraw
import org.parceler.Parcels

class ChooseUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        val withdrawalParcelable =
            if (intent.hasExtra(WITHDRAWAL_EXTRA)) intent.getParcelableExtra(WITHDRAWAL_EXTRA) as Parcelable else null

        val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw
        val fragment = UsersFragment.newInstance(withdrawal)

        supportFragmentManager.beginTransaction().add(R.id.chooseUserFragment, fragment, "1").commit()
    }
}
