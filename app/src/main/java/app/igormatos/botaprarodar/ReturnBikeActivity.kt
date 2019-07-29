package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import app.igormatos.botaprarodar.model.Withdraw
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_return_bike.*
import org.parceler.Parcels
import java.util.*

val WITHDRAWAL_EXTRA = "WITHDRAWAL_EXTRA"

class ReturnBikeActivity : AppCompatActivity() {

    private val withdrawalsReference = FirebaseDatabase.getInstance().getReference("withdrawals")
    private var withdrawalToSend = Withdraw()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_bike)

        val withdrawalParcelable =
            if (intent.hasExtra(WITHDRAWAL_EXTRA)) intent.getParcelableExtra(WITHDRAWAL_EXTRA) as Parcelable else null

        val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw
        withdrawalToSend = withdrawal

        userName.text = withdrawal.user_name
        withdrawal.user?.let { userDoc.text = it.doc_number.toString() }

        bikeName.text = withdrawal.bicycle_name

        bicycleImageView.loadPath(withdrawal.bicycle_image_path!!)
        userImageView.loadPath(withdrawal.user_image_path!!)

        confirmBikeReturn.setOnClickListener {

            if (!isSurveyAnswered()) {
                Toast.makeText(this, "É obrigatório responder o questionário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = Calendar.getInstance().time
            withdrawalToSend.returned_date = date.time
            withdrawalToSend.modified_time = date.time

            withdrawalsReference.child(withdrawal.id!!).setValue(withdrawalToSend).addOnSuccessListener {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        fillRideQuiz.setOnClickListener {
            val intent = Intent(this@ReturnBikeActivity, RideQuizActivity::class.java)
            startActivityForResult(intent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 10) {
            data?.let {
                val withdrawalParcelable = if (it.hasExtra(WITHDRAWAL_EXTRA)) it.getParcelableExtra(WITHDRAWAL_EXTRA) as Parcelable else null
                val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw
                withdrawalToSend.bicycle_rating = withdrawal.bicycle_rating
                withdrawalToSend.trip_reason = withdrawal.trip_reason
                withdrawalToSend.destination = withdrawal.destination
            }
        }
    }

    fun isSurveyAnswered() : Boolean {
        return withdrawalToSend.bicycle_rating != null &&
                withdrawalToSend.trip_reason != null &&
                withdrawalToSend.destination != null
    }
}
