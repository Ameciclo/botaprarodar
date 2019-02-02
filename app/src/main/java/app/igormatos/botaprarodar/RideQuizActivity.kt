package app.igormatos.botaprarodar

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.igormatos.botaprarodar.model.Withdraw
import kotlinx.android.synthetic.main.activity_ride_quiz.*
import org.parceler.Parcels

class RideQuizActivity : AppCompatActivity() {

    val quiz: Withdraw = Withdraw()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_quiz)

        bikeRateGroup.setOnCheckedChangeListener { group, checkedId ->
            quiz.bicycle_rating = when(checkedId) {
                R.id.badCheck -> "ruim"
                R.id.regularCheck -> "regular"
                R.id.goodCheck -> "boa"
                R.id.greatCheck -> "otima"
                else -> "não respondeu"
            }
        }

        rideTypeGroup.setOnCheckedChangeListener { group, checkedId ->
            quiz.trip_reason = when(checkedId) {
                R.id.buyCheck -> "compras"
                R.id.workCheck -> "trabalho"
                R.id.workUseCheck -> "uso a trabalho"
                R.id.recreationCheck -> "lazer"
                R.id.otherCheck -> "outros"
                else -> "não respondeu"
            }
        }

        confirmQuizButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, quiz))
            setResult(10, intent)
            finish()
        }
    }
}
