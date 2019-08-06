package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.util.REQUEST_PHOTO
import app.igormatos.botaprarodar.util.takePictureIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_add_bike.*
import org.parceler.Parcels

val BIKE_EXTRA = "Bike_extra"

class AddBikeActivity : AppCompatActivity() {

    var bicycleToAdd = Bicycle()
    var editMode: Boolean = false
    var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bike)

        bikePhotoImageView.setOnClickListener {
            takePictureIntent(REQUEST_PHOTO) { path ->
                this.imagePath = path
            }
        }

        saveButton.setOnClickListener {
            if (hasEmptyField()) {
                Toast.makeText(this@AddBikeActivity, getString(R.string.empties_fields_error), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            saveButton.isEnabled = false

            if (editMode) {
                addBikeToServer()
                return@setOnClickListener
            } else {
                uploadImage { addBikeToServer() }
            }

        }

        val userParcelable =
            if (intent.hasExtra(BIKE_EXTRA)) intent.getParcelableExtra(BIKE_EXTRA) as Parcelable else null
        checkIfEditMode(userParcelable)

        toolbar.title = if (editMode) {
            getString(R.string.bicycle_update_button)
        } else {
            getString(R.string.bicycle_add_button)
        }
    }

    private fun checkIfEditMode(parcelable: Parcelable?) {
        if (parcelable == null) return

        setupBicycle(parcelable)
    }

    private fun setupBicycle(userParcelable: Parcelable) {
        editMode = true
        val bicycle = Parcels.unwrap(userParcelable) as Bicycle

        bicycleToAdd = bicycle

        bikePhotoImageView.setOnClickListener {
            val fullscreenIntent = Intent(this, FullscreenImageActivity::class.java)
            fullscreenIntent.putExtra(EXTRA_IMAGE_PATH, bicycleToAdd.photo_path)
            startActivity(fullscreenIntent)
        }

        bicycle.photo_path?.let { bikePhotoImageView.loadPath(it) }
        serieNumber.setText(bicycle.serial_number)
        bikeName.setText(bicycle.name)
        orderNumber.setText(bicycle.order_number.toString())

        saveButton.text = getString(R.string.update_button)
    }

    fun hasEmptyField(): Boolean {
        return orderNumber.text.isNullOrEmpty() ||
                serieNumber.text.isNullOrEmpty() ||
                bikeName.text.isNullOrEmpty() ||
                bikePhotoImageView.drawable == null
    }

    fun addBikeToServer() {
        bicycleToAdd.name = bikeName.text.toString()
        bicycleToAdd.serial_number = serieNumber.text.toString()
        bicycleToAdd.order_number = orderNumber.text.toString().toLong()

        bicycleToAdd.saveRemote { success ->
            if (success) {
                Toast.makeText(this@AddBikeActivity, successText(), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                saveButton.isEnabled = true
            }
        }
    }

    fun successText(): String {
        return if (editMode) getString(R.string.bicycle_update_success) else getString(R.string.bicycle_add_success)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            imagePath?.let {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(bikePhotoImageView)
            }

        }
    }

    private fun uploadImage(afterSuccess: () -> Unit) {
        imagePath?.let {
            FirebaseHelper.uploadImage(it) { success, path ->
                if (success) {
                    bicycleToAdd.photo_path = path
                    afterSuccess()
                } else {
                    Toast.makeText(
                        this, getString(R.string.something_happened_error), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}
