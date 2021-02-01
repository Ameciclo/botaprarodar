package app.igormatos.botaprarodar.presentation.adduser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.databinding.ActivityAddUserBinding
import app.igormatos.botaprarodar.databinding.ActivityBikeFormBinding
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.fullscreenimage.FullscreenImageActivity
import com.brunotmgomes.ui.extensions.loadPath
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_add_user.*
import org.jetbrains.anko.image
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel


val USER_EXTRA = "USER_EXTRA"

class AddUserActivity : AppCompatActivity() {

    val REQUEST_PROFILE_PHOTO = 1
    val REQUEST_ID_PHOTO = 2
    val REQUEST_RESIDENCE_PHOTO = 3
    val REQUEST_ID_PHOTO_BACK = 4

    var userToSend = User()
    var userCopy = User()
    var profilePhotoHasChanged: Boolean = false
    var idPhotoHasChanged: Boolean = false
    var idBackPhotosHasChanged: Boolean = false
    var residencePhotoHasChanged: Boolean = false

    private val binding: ActivityAddUserBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddUserBinding>(this, R.layout.activity_add_user)
    }

    private val addUserViewModel: AddUserViewModel by koinViewModel()

    lateinit var mCurrentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        binding.lifecycleOwner = this
        binding.viewModel = addUserViewModel

        binding.profileImageView.setOnClickListener {
            showTipDialog(
                R.drawable.iconfinder_user_profile_imagee,
                getString(R.string.profile_picture),
                getString(R.string.profile_picture_tip)
            ) {
                if (it) {
                    dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO)
                }
            }
        }

        binding.ivFrontDocument.setOnClickListener {
            showTipDialog(
                R.drawable.id_front,
                getString(R.string.warning),
                getString(R.string.id_picture_tip)
            ) {
                if (it) {
                    dispatchTakePictureIntent(REQUEST_ID_PHOTO)
                }
            }
        }

        binding.ivBackDocument.setOnClickListener {
            showTipDialog(
                R.drawable.id_back,
                getString(R.string.warning),
                getString(R.string.id_picture_tip)
            ) {
                if (it) {
                    dispatchTakePictureIntent(REQUEST_ID_PHOTO_BACK)
                }
            }
        }

        binding.ivResidenceProof.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
        }

        val userParcelable: Parcelable? =
            if (intent.hasExtra(USER_EXTRA)) intent.getParcelableExtra(
                USER_EXTRA
            ) else null
        checkIfEditMode(userParcelable)

    }

    private fun checkIfEditMode(userParcelable: Parcelable?) {
        if (userParcelable == null) return
    }


    private fun addUserToServer() {

        userToSend.saveRemote { success ->
            if (success) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@AddUserActivity,
                    getString(R.string.operation_success_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
                finish()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@AddUserActivity,
                    getString(R.string.something_happened_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
                saveButton.isEnabled = true
            }
        }

    }

//    private fun hasEmptyField(): Boolean {
//        return completeNameField.text.isNullOrEmpty() ||
//                addressField.text.isNullOrEmpty() ||
//                idNumberField.text.isNullOrEmpty() ||
//                userToSend.doc_picture.isNullOrEmpty() ||
//                userToSend.profile_picture.isNullOrEmpty() ||
//                userToSend.residence_proof_picture.isNullOrEmpty() ||
//                userToSend.doc_picture_back.isNullOrEmpty()
//    }

    private fun uploadImage(whichImageCode: Int) {

        FirebaseHelper.uploadImage(mCurrentPhotoPath) { success, path, thumbPath ->
            if (success) {
                updateUserImagePath(whichImageCode, path.toString(), thumbPath.toString())
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.something_happened_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun updateUserImagePath(whichImageCode: Int, newPath: String, thumbPath: String) {
        when (whichImageCode) {
            REQUEST_PROFILE_PHOTO -> {
                userToSend.profile_picture = newPath
                userToSend.profile_picture_thumbnail = thumbPath
            }
            REQUEST_ID_PHOTO -> {
                userToSend.doc_picture = newPath
            }
            REQUEST_RESIDENCE_PHOTO -> {
                userToSend.residence_proof_picture = newPath
            }
            REQUEST_ID_PHOTO_BACK -> {
                userToSend.doc_picture_back = newPath
            }

        }
    }

    private fun dispatchTakePictureIntent(code: Int) {
        takePictureIntent(code) { path ->
            mCurrentPhotoPath = path
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

//            val loadImageView = when (requestCode) {
//                REQUEST_PROFILE_PHOTO -> {
//                    profilePhotoHasChanged = true
//                    uploadImage(requestCode)
//                    profileImageView
//                }
//
//                REQUEST_ID_PHOTO -> {
//                    idPhotoHasChanged = true
//                    idFrontImageView
//                }
//
//                REQUEST_RESIDENCE_PHOTO -> {
//                    residencePhotoHasChanged = true
//                    residenceProofImageView
//                }
//
//                REQUEST_ID_PHOTO_BACK -> {
//                    idBackPhotosHasChanged = true
//                    idBackImageView
//                }
//
//                else -> profileImageView
//            }

            uploadImage(requestCode)

            Log.d("BFLW-PICTURE", "Image path $mCurrentPhotoPath")

//            loadImageView.loadPath(mCurrentPhotoPath)
        }
    }

    private fun showTipDialog(
        image: Int,
        title: String,
        subtitle: String,
        click: (Boolean) -> Unit
    ) {
        val tipLayout = layoutInflater.inflate(R.layout.dialog_tip, null)

        tipLayout.findViewById<ImageView>(R.id.tipImage).image =
            ContextCompat.getDrawable(this, image)
        tipLayout.findViewById<TextView>(R.id.tipTitle).text = title
        tipLayout.findViewById<TextView>(R.id.tipSubtitle).text = subtitle

        MaterialAlertDialogBuilder(this)
            .setView(tipLayout)
            .setPositiveButton("Tirar foto!") { _, _ ->
                click(true)
            }
            .show()

    }

}
