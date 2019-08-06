package app.igormatos.botaprarodar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.igormatos.botaprarodar.local.model.Withdraw
import kotlinx.android.synthetic.main.activity_ride_quiz.*
import org.parceler.Parcels

class RideQuizActivity : AppCompatActivity() {

    val withdrawQuiz: Withdraw = Withdraw()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_quiz)

        bikeRateGroup.setOnCheckedChangeListener { _, checkedId ->
            withdrawQuiz.bicycle_rating = when (checkedId) {
                R.id.badCheck -> getString(R.string.withdrawal_bicycle_rating_bad)
                R.id.regularCheck -> getString(R.string.withdrawal_bicycle_rating_regular)
                R.id.goodCheck -> getString(R.string.withdrawal_bicycle_rating_good)
                R.id.greatCheck -> getString(R.string.withdrawal_bicycle_rating_great)
                else -> "não respondeu"
            }
        }

        rideTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            withdrawQuiz.trip_reason = when (checkedId) {
                R.id.buyCheck -> getString(R.string.trip_reason_shopping)
                R.id.workCheck -> getString(R.string.trip_reason_to_work)
                R.id.workUseCheck -> getString(R.string.trip_reason_bicycle_as_work)
                R.id.recreationCheck -> getString(R.string.trip_reason_leisure)
                R.id.otherCheck -> getString(R.string.trip_reason_other)
                else -> "não respondeu"
            }
        }

        confirmQuizButton.setOnClickListener {
            val intent = Intent()

            withdrawQuiz.destination = quizDestination.text.toString()

            intent.putExtra(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, withdrawQuiz))
            setResult(10, intent)
            finish()
        }
    }
}
