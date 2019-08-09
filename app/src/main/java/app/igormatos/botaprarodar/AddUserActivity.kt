package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import app.igormatos.botaprarodar.local.model.User
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.util.takePictureIntent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_add_user.*
import org.jetbrains.anko.image
import org.parceler.Parcels

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
    var residencePhotoHasChanged: Boolean = false

    lateinit var mCurrentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        profileImageView.setOnClickListener {
            showTipDialog(R.drawable.iconfinder_face_id,
                "Foto do perfil",
                "Tire uma foto dos ombros para cima, estilo 3x4, procure locais claros, se aproxime da pessoa e verifique se é possível identificá-la depois.") {
                if (it) { dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO) }
            }
        }

        idFrontImageView.setOnClickListener {
            showTipDialog(R.drawable.id_front,
                getString(R.string.warning),
                getString(R.string.id_picture_tip)) {
                if (it) { dispatchTakePictureIntent(REQUEST_ID_PHOTO) }
            }
        }

        idBackImageView.setOnClickListener {
            showTipDialog(R.drawable.id_back,
                "Atenção",
                "Tire uma foto clara que enquadre todo o documento") {
                if (it) { dispatchTakePictureIntent(REQUEST_ID_PHOTO_BACK) }
            }
        }

        residenceProofImageView.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
        }

        saveButton.setOnClickListener {
            if (hasEmptyField()) {
                Toast.makeText(this, getString(R.string.empties_fields_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveButton.isEnabled = false
            addUserToServer()
        }

        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.maleCheck -> {
                    userToSend.gender = 0
                }

                R.id.femaleCheck -> {
                    userToSend.gender = 1
                }

                R.id.otherCheck -> {
                    userToSend.gender = 2
                }

                R.id.dontNeedCheck -> {
                    userToSend.gender = 3
                }
            }
        }

        genderRadioGroup.check(R.id.dontNeedCheck)

        val userParcelable =
            if (intent.hasExtra(USER_EXTRA)) intent.getParcelableExtra(USER_EXTRA) as Parcelable else null
        checkIfEditMode(userParcelable)

    }

    private fun checkIfEditMode(userParcelable: Parcelable?) {
        if (userParcelable == null) return

        setupUser(userParcelable)
    }

    private fun setupUser(userParcelable: Parcelable) {
        val user = Parcels.unwrap(userParcelable) as User

        userToSend = user
        userCopy = user

        profileImageView.setOnClickListener {
            val fullscreenIntent = Intent(this, FullscreenImageActivity::class.java)
            fullscreenIntent.putExtra(EXTRA_IMAGE_PATH, user.profile_picture)
            startActivity(fullscreenIntent)
        }

        idFrontImageView.setOnClickListener {
            val fullscreenIntent = Intent(this, FullscreenImageActivity::class.java)
            fullscreenIntent.putExtra(EXTRA_IMAGE_PATH, user.doc_picture)
            startActivity(fullscreenIntent)
        }

        residenceProofImageView.setOnClickListener {
            val fullscreenIntent = Intent(this, FullscreenImageActivity::class.java)
            fullscreenIntent.putExtra(EXTRA_IMAGE_PATH, user.residence_proof_picture)
            startActivity(fullscreenIntent)
        }

        editProfilePhotoButton.visibility = View.VISIBLE
        editProfilePhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO) }

        idFrontImageView.setOnLongClickListener {
            dispatchTakePictureIntent(REQUEST_ID_PHOTO)
            return@setOnLongClickListener true
        }

//        editIdPhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_ID_PHOTO) }

        editResidencePhotoButton.visibility = View.VISIBLE
        editResidencePhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO) }

        user.profile_picture?.let { profileImageView.loadPath(it) }
        user.doc_picture?.let { idFrontImageView.loadPath(it) }
        user.residence_proof_picture?.let { residenceProofImageView.loadPath(it) }
        user.name?.let { completeNameField.setText(it) }

        when (user.doc_type) {
            1 -> {
                idLayout.hint = "Número do RG"
            }
            2 -> {
                idLayout.hint = "Número do CPF"
            }
        }

        when (user.gender) {
            0 -> {
                maleCheck.isChecked = true
            }
            1 -> {
                femaleCheck.isChecked = true
            }
            2 -> {
                otherCheck.isChecked = true
            }
            3 -> {
                dontNeedCheck.isChecked = true
            }
        }


        idNumberField.setText(user.doc_number.toString())


        user.address?.let { addressField.setText(it) }
        saveButton.text = getString(R.string.update_button)
    }

    private fun addUserToServer() {
        progressBar.visibility = View.VISIBLE
        userToSend.name = completeNameField.text.toString()
        userToSend.address = addressField.text.toString()
        userToSend.doc_number = idNumberField.text.toString().toLong()

        userToSend.saveRemote { success ->
            if (success) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddUserActivity, getString(R.string.operation_success_message), Toast.LENGTH_SHORT)
                    .show()
                finish()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddUserActivity, getString(R.string.something_happened_error), Toast.LENGTH_SHORT)
                    .show()
                saveButton.isEnabled = true
            }
        }

    }

    private fun hasEmptyField(): Boolean {
        return completeNameField.text.isNullOrEmpty() ||
                addressField.text.isNullOrEmpty() ||
                idNumberField.text.isNullOrEmpty() ||
                userToSend.doc_picture.isNullOrEmpty() ||
                userToSend.profile_picture.isNullOrEmpty() ||
                userToSend.residence_proof_picture.isNullOrEmpty()
    }

    private fun uploadImage(whichImageCode: Int) {

        FirebaseHelper.uploadImage(mCurrentPhotoPath) { success, path, thumbPath ->
            if (success) {
                updateUserImagePath(whichImageCode, path.toString(), thumbPath.toString())
            } else {
                Toast.makeText(
                    this, getString(R.string.something_happened_error), Toast.LENGTH_SHORT
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

            val loadImageView = when (requestCode) {
                REQUEST_PROFILE_PHOTO -> {
                    profilePhotoHasChanged = true
                    userToSend.profile_picture = mCurrentPhotoPath
                    uploadImage(requestCode)
                    profileImageView
                }

                REQUEST_ID_PHOTO -> {
                    idPhotoHasChanged = true
                    userToSend.doc_picture = mCurrentPhotoPath
                    uploadImage(requestCode)
                    idFrontImageView
                }

                REQUEST_RESIDENCE_PHOTO -> {
                    residencePhotoHasChanged = true
                    userToSend.residence_proof_picture = mCurrentPhotoPath
                    uploadImage(requestCode)
                    residenceProofImageView
                }

                else -> profileImageView
            }

            Log.d("BFLW-PICTURE", "Image path $mCurrentPhotoPath")

            loadImageView.loadPath(mCurrentPhotoPath)
        }
    }

    private fun showTipDialog(image: Int, title: String, subtitle: String, click: (Boolean) -> Unit) {
        val tipLayout = layoutInflater.inflate(R.layout.dialog_tip, null)

        tipLayout.findViewById<ImageView>(R.id.tipImage).image = ContextCompat.getDrawable(this, image)
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
