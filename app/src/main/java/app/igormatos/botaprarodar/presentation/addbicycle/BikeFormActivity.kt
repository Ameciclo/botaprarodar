package app.igormatos.botaprarodar.presentation.addbicycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.databinding.ActivityBikeFormBinding
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import com.brunotmgomes.ui.extensions.showLoadingDialog
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

val BIKE_EXTRA = "Bike_extra"

class BikeFormActivity : AppCompatActivity() {

    var editMode: Boolean = false
    var imagePath: String? = null

    private val formViewModel: BikeFormViewModel by koinViewModel()

    private val binding: ActivityBikeFormBinding by lazy {
        DataBindingUtil.setContentView<ActivityBikeFormBinding>(this, R.layout.activity_bike_form)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = formViewModel

        onClickBicyclePhotoImage()
        waitBicycleRegisterResult()

        binding.toolbar.title = if (editMode) {
            getString(R.string.bicycle_update_button)
        } else {
            getString(R.string.bicycle_add_button)
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
        binding.viewModel?.state?.observe(this, Observer { bikeFormStatus ->
            when (bikeFormStatus) {
                is BikeFormStatus.Success -> {
                    bikeFormStatus.data
                    successText()
                }
                is BikeFormStatus.Loading -> {}
                is BikeFormStatus.Error -> errorText(bikeFormStatus.message)
            }
        })
    }

    private fun successText(){
//         (editMode) getString(R.string.bicycle_update_success) else
        showMessage( getString(R.string.bicycle_add_success))
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
