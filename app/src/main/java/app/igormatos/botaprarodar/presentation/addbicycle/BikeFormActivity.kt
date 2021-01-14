package app.igormatos.botaprarodar.presentation.addbicycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import app.igormatos.botaprarodar.presentation.fullscreenimage.FullscreenImageActivity
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import com.brunotmgomes.ui.extensions.loadPath
import com.brunotmgomes.ui.extensions.showLoadingDialog
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel
import org.parceler.Parcels

val BIKE_EXTRA = "Bike_extra"

class BikeFormActivity : AppCompatActivity() {

    var bicycleToAdd = Bike()
    var editMode: Boolean = false
    var imagePath: String? = null
    var loadingDialog: AlertDialog? = null

    private val formViewModel: BikeFormViewModel by koinViewModel()

    private val binding: ActivityBikeFormBinding by lazy {
        DataBindingUtil.setContentView<ActivityBikeFormBinding>(this, R.layout.activity_bike_form)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike_form)

        binding.lifecycleOwner = this
        binding.viewModel = formViewModel

        val bicycleParcelable: Parcelable? =
            if (intent.hasExtra(BIKE_EXTRA)) intent.getParcelableExtra(BIKE_EXTRA) else null
        checkIfEditMode(bicycleParcelable)

        onClickBicyclePhotoImage()

        waitBicycleRegisterResult()

        /* binding.saveButton.setOnClickListener {
             if (hasEmptyField()) {
                 Toast.makeText(
                     this@AddBikeActivity,
                     getString(R.string.empties_fields_error),
                     Toast.LENGTH_SHORT
                 )
                     .show()
                 return@setOnClickListener
             }

             binding.saveButton.isEnabled = false

             if (editMode) {
                 addBikeToServer()
                 return@setOnClickListener
             } else {
                 uploadImage { addBikeToServer() }
             }

         }*/

        binding.toolbar.title = if (editMode) {
            getString(R.string.bicycle_update_button)
        } else {
            getString(R.string.bicycle_add_button)
        }

    }

    private fun waitBicycleRegisterResult() {
        binding.viewModel?.state?.observe(this, Observer { netWorkResource ->
            when (netWorkResource) {
                is BikeFormStatus.Success -> {
                    netWorkResource.data
                    successText()
                }
                is BikeFormStatus.Loading -> TODO()
            }
        })
    }

    private fun onClickBicyclePhotoImage() {
        binding.bikePhotoImageView.setOnClickListener {
            takePictureIntent(REQUEST_PHOTO) { path -> this.imagePath = path }
        }
    }


    private fun checkIfEditMode(parcelable: Parcelable?) {
        if (parcelable == null) return

        setupBicycle(parcelable)
    }

    private fun setupBicycle(bicycleParcelable: Parcelable) {
        editMode = true
        val bicycle = Parcels.unwrap(bicycleParcelable) as Bike

        bicycleToAdd = bicycle

        binding.bikePhotoImageView.setOnClickListener {
            FullscreenImageActivity.start(this, bicycleToAdd.photo_path)
        }

        bicycle.photo_path?.let { binding.bikePhotoImageView.loadPath(it) }
        binding.serieNumber.setText(bicycle.serial_number)
        binding.bikeName.setText(bicycle.name)
        binding.orderNumber.setText(bicycle.order_number.toString())

        binding.saveButton.text = getString(R.string.update_button)
    }

    fun hasEmptyField(): Boolean {
        return binding.orderNumber.text.isNullOrEmpty() ||
                binding.serieNumber.text.isNullOrEmpty() ||
                binding.bikeName.text.isNullOrEmpty() ||
                binding.bikePhotoImageView.drawable == null
    }

    fun addBikeToServer() {
        bicycleToAdd.name = binding.bikeName.text.toString()
        bicycleToAdd.serial_number = binding.serieNumber.text.toString()
        bicycleToAdd.order_number = binding.orderNumber.text.toString().toLong()

        bicycleToAdd.saveRemote { success ->
            if (success) {
                Toast.makeText(this@BikeFormActivity, successText(), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.saveButton.isEnabled = true
            }

            loadingDialog?.dismiss()
        }
    }

    fun successText(): String {
        return if (editMode) getString(R.string.bicycle_update_success) else getString(
            R.string.bicycle_add_success
        )
    }

    fun errorText(): String {
        return if (editMode) getString(R.string.bicycle_update_success) else getString(
            R.string.bicycle_add_success
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            imagePath?.let {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(binding.bikePhotoImageView)
            }

        }
    }

    private fun uploadImage(afterSuccess: () -> Unit) {
        loadingDialog = showLoadingDialog(R.layout.loading_dialog_animation)

        imagePath?.let {
            FirebaseHelper.uploadImage(it) { success, path, thumbnailPath ->
                if (success) {
                    bicycleToAdd.photo_path = path
                    bicycleToAdd.photo_thumbnail_path = thumbnailPath
                    afterSuccess()
                } else {
                    loadingDialog?.dismiss()
                    binding.saveButton.isEnabled = true
                    Toast.makeText(
                        this, getString(R.string.something_happened_error), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}
