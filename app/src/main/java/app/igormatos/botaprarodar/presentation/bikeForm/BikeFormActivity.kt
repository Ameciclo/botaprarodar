package app.igormatos.botaprarodar.presentation.bikeForm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.databinding.ActivityBikeFormBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels

class BikeFormActivity : AppCompatActivity() {

    var imagePath: String? = null
    private lateinit var loadingDialog: AlertDialog

    private val formViewModel: BikeFormViewModel by viewModel()

    private val binding: ActivityBikeFormBinding by lazy {
        DataBindingUtil.setContentView<ActivityBikeFormBinding>(this, R.layout.activity_bike_form)
    }

    companion object {
        const val BIKE_EXTRA = "Bike_extra"

        fun setupActivity(context: Context, bike: Bike?): Intent {
            val intent = Intent(context, BikeFormActivity::class.java)
            intent.putExtra(BIKE_EXTRA, bike)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = formViewModel

        setupToolbar()
        onClickBicyclePhotoImage()
        waitBicycleRegisterResult()
        checkEditMode()
        loadingDialog = createLoading(R.layout.loading_dialog_animation)

    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun checkEditMode() {
        if (intent.extras != null) {
            val userExtra = intent.extras?.getParcelable<Bike>(BIKE_EXTRA)
            setValuesToEditBike(userExtra)
        }
//        val parcelableBike: Parcelable? =
//            if (intent.hasExtra(BIKE_EXTRA)) intent.getParcelableExtra(BIKE_EXTRA) else null

//        val parcelableBike = intent.getParcelableExtra<Bike>(BIKE_EXTRA)


//        if (parcelableBike != null) {
//            val bike = Parcels.unwrap(parcelableBike) as Bike
//            setValuesToEditBike(bike)
//        }
    }

    private fun onClickBicyclePhotoImage() {
        binding.bikePhotoImageView.setOnClickListener {
            takePictureIntent(REQUEST_PHOTO) { path ->
                this.imagePath = path
            }
        }

        binding.addPhotoTextView.setOnClickListener {
            takePictureIntent(REQUEST_PHOTO) { path ->
                this.imagePath = path
            }
        }
    }

    private fun waitBicycleRegisterResult() {
        binding.viewModel?.state?.observe(this, Observer { bikeFormStatus ->
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
        val intent = Intent().putExtra("isEditModeAvailable", formViewModel.isEditModeAvailable)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun errorText(errorMessage: String) {
        snackBarMaker(errorMessage, binding.containerAddBike).apply {
            setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red))
            show()
        }
    }

    private fun setValuesToEditBike(bike: Bike?) {
        bike?.let {
            formViewModel.updateBikeValues(it)
            setImageVisibilities()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            imagePath?.let {
                binding.viewModel?.updateImagePath(it)
                setImageVisibilities()
            }
        }
    }

    private fun setImageVisibilities() {
        binding.cameraImageView.visible()
        binding.addPhotoTextView.gone()
    }
}
