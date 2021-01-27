package app.igormatos.botaprarodar.presentation.addbicycle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.databinding.ActivityBikeFormBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.hideKeyboard
import com.brunotmgomes.ui.extensions.takePictureIntent
import org.parceler.Parcels
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class BikeFormActivity : AppCompatActivity() {

    var editMode: Boolean = false
    var imagePath: String? = null
    private lateinit var loadingDialog: AlertDialog

    private val formViewModel: BikeFormViewModel by koinViewModel()

    private val binding: ActivityBikeFormBinding by lazy {
        DataBindingUtil.setContentView<ActivityBikeFormBinding>(this, R.layout.activity_bike_form)
    }

    companion object {
        const val BIKE_EXTRA = "Bike_extra"

        fun setupActivity(context: Context, bike: Bike?): Intent {
            val intent = Intent(context, BikeFormActivity::class.java)
            intent.putExtra(BIKE_EXTRA, Parcels.wrap(Bike::class.java, bike))
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = formViewModel

        onClickBicyclePhotoImage()
        waitBicycleRegisterResult()
        checkEditMode()
        loadingDialog = createLoading(R.layout.loading_dialog_animation)

        binding.toolbar.title = if (editMode) {
            getString(R.string.bicycle_update_button)
        } else {
            getString(R.string.bicycle_add_button)
        }
    }

    private fun checkEditMode() {
        val parcelableBike: Parcelable? =
            if (intent.hasExtra(BIKE_EXTRA)) intent.getParcelableExtra(BIKE_EXTRA) else null

        if(parcelableBike != null) {
            editMode = true
        }
    }

    private fun onClickBicyclePhotoImage() {
        binding.bikePhotoImageView.setOnClickListener {
            takePictureIntent(REQUEST_PHOTO) { path ->
                this.imagePath = path
            }
        }
    }

    private fun waitBicycleRegisterResult() {
        binding.viewModel?.state?.observe(this, { bikeFormStatus ->
            when (bikeFormStatus) {
                is BikeFormStatus.Success -> {
                    loadingDialog.dismiss()
                    successText()
                }
                is BikeFormStatus.Loading -> {
                    window.decorView.hideKeyboard()
                    loadingDialog.show()
                }
                is BikeFormStatus.Error -> {
                    loadingDialog.dismiss()
                    errorText(bikeFormStatus.message)
                }
            }
        })
    }

    private fun successText() {
        showMessage(getString(R.string.bicycle_add_success))
        finish()
    }

    private fun errorText(errorMessage: String) {
        showMessage(errorMessage)
        finish()
    }

    private fun showMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            imagePath?.let {
                binding.viewModel?.updateImagePath(it)
            }
        }
    }
}
