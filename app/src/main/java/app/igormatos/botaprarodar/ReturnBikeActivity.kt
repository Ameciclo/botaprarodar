package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import app.igormatos.botaprarodar.model.Withdraw
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_user.*
import kotlinx.android.synthetic.main.activity_return_bike.*
import org.parceler.Parcels
import java.text.SimpleDateFormat
import java.util.*

val WITHDRAWAL_EXTRA = "WITHDRAWAL_EXTRA"

class ReturnBikeActivity : AppCompatActivity() {

    private val withdrawalsReference = FirebaseDatabase.getInstance().getReference("withdrawals")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_bike)

        val withdrawalParcelable =
            if (intent.hasExtra(WITHDRAWAL_EXTRA)) intent.getParcelableExtra(WITHDRAWAL_EXTRA) as Parcelable else null

        val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw

        userName.text = withdrawal.user_name
        withdrawal.user?.let { userDoc.text = it.doc_number.toString() }

        bikeName.text = withdrawal.bicycle_name

        bicycleImageView.loadPath(withdrawal.bicycle_image_path!!)
        userImageView.loadPath(withdrawal.user_image_path!!)

        confirmBikeReturn.setOnClickListener {
            val date = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            withdrawal.returned_date = dateFormat.format(date)

            withdrawalsReference.child(withdrawal.id!!).setValue(withdrawal).addOnSuccessListener {
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
    }
}
