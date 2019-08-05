package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Withdraw
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.util.showLoadingDialog
import kotlinx.android.synthetic.main.activity_return_bike.*
import org.parceler.Parcels
import java.util.*

val WITHDRAWAL_EXTRA = "WITHDRAWAL_EXTRA"
val WITHDRAWAL_BICYCLE = "BICYCLE_EXTRA"

class ReturnBikeActivity : AppCompatActivity() {

    private var loadingDialog: AlertDialog? = null
    private var withdrawal: Withdraw = Withdraw()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_bike)

        getWithdrawal()

        confirmBikeReturn.setOnClickListener {

            if (!isSurveyAnswered()) {
                Toast.makeText(this, getString(R.string.fill_quiz_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = Calendar.getInstance().time
            withdrawal.returned_date = date.time
            withdrawal.modified_time = date.time

            withdrawal.updateRemote {
                FirebaseHelper.updateBicycleStatus(withdrawal.bicycle_id!!, true) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

        }

        fillRideQuiz.setOnClickListener {
            val intent = Intent(this@ReturnBikeActivity, RideQuizActivity::class.java)
            startActivityForResult(intent, 10)
        }
    }

    fun getWithdrawal() {
        loadingDialog = showLoadingDialog()

        val bicycleParcelable = (if (intent.hasExtra(WITHDRAWAL_BICYCLE)) {
            intent.getParcelableExtra<Parcelable>(WITHDRAWAL_BICYCLE)
        } else {
            null
        }) ?: return

        val bicycle = Parcels.unwrap<Bicycle>(bicycleParcelable)

        FirebaseHelper.getWithdrawalFromBicycle(bicycleId = bicycle.id!!) {
            loadingDialog?.dismiss()

            if (it == null) {
                return@getWithdrawalFromBicycle
            }

            withdrawal = it
            setWithdrawInfo(it)
        }

    }

    fun setWithdrawInfo(withdraw: Withdraw) {
        userName.text = withdraw.user_name
        withdraw.user?.let { userDoc.text = it.doc_number.toString() }
        bikeName.text = withdraw.bicycle_name
        bicycleImageView.loadPath(withdraw.bicycle_image_path!!)
        userImageView.loadPath(withdraw.user_image_path!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 10) {
            data?.let {
                val withdrawalParcelable =
                    if (it.hasExtra(WITHDRAWAL_EXTRA)) it.getParcelableExtra(WITHDRAWAL_EXTRA) as Parcelable else null
                val withdrawal = Parcels.unwrap(withdrawalParcelable) as Withdraw
                this.withdrawal.bicycle_rating = withdrawal.bicycle_rating
                this.withdrawal.trip_reason = withdrawal.trip_reason
                this.withdrawal.destination = withdrawal.destination
            }
        }
    }

    private fun isSurveyAnswered(): Boolean {
        return withdrawal.bicycle_rating != null &&
                withdrawal.trip_reason != null &&
                withdrawal.destination != null
    }
}
