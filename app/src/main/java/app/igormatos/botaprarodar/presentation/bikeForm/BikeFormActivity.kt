package app.igormatos.botaprarodar.presentation.bikeForm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.databinding.ActivityBikeFormBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.extensions.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class BikeFormActivity : AppCompatActivity() {
    private val binding: ActivityBikeFormBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_bike_form)
    }

    private var imagePath: String? = null
    private lateinit var formViewModel: BikeFormViewModel
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val communityBikes = getCommunityBikes()
        formViewModel = setupViewModel(communityBikes)
        setupBinding(formViewModel)
        setupLoadingDialog()
        setupToolbar()
        onClickBicyclePhotoImage()
        waitBicycleRegisterResult()
        checkEditMode()
    }

    private fun getCommunityBikes(): ArrayList<Bike> {
        if (intent.hasExtra(COMMUNITY_BIKES_EXTRA)) {
            return intent.extras?.getParcelableArrayList(COMMUNITY_BIKES_EXTRA)!!
        }
        return arrayListOf()
    }

    private fun setupViewModel(communityBikes: ArrayList<Bike>): BikeFormViewModel {
        formViewModel = getViewModel {
            parametersOf(communityBikes)
        }
        return formViewModel
    }

    private fun setupBinding(formViewModel: BikeFormViewModel) {
        binding.lifecycleOwner = this
        binding.viewModel = formViewModel
    }

    private fun setupLoadingDialog() {
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
            if (userExtra != null ) setValuesToEditBike(userExtra)
        }
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
                    errorText(this.getString(bikeFormStatus.messageResId))
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

    companion object {
        const val BIKE_EXTRA = "bike_extra"
        private const val COMMUNITY_BIKES_EXTRA = "community_bikes_extra"

        fun setupActivity(context: Context, bike: Bike?, communityBikes: ArrayList<Bike>): Intent {
            val intent = Intent(context, BikeFormActivity::class.java)
            intent.putExtra(BIKE_EXTRA, bike)
            intent.putParcelableArrayListExtra(COMMUNITY_BIKES_EXTRA, communityBikes)
            return intent
        }
    }
}
