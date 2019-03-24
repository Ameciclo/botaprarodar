package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import app.igormatos.botaprarodar.model.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_user.*
import org.parceler.Parcels
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

val USER_EXTRA = "USER_EXTRA"

class AddUserActivity : AppCompatActivity() {

    val REQUEST_PROFILE_PHOTO = 1
    val REQUEST_ID_PHOTO = 2
    val REQUEST_RESIDENCE_PHOTO = 3

    var userToSend = User()
    var usersReference = FirebaseDatabase.getInstance().getReference("users")
    var userCopy = User()
    var profilePhotoHasChanged: Boolean = false
    var idPhotoHasChanged: Boolean = false
    var residencePhotoHasChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        profileImageView.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO)
        }

        idImageView.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_ID_PHOTO)
        }

        residenceProofImageView.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
        }

        saveButton.setOnClickListener {
            if (hasEmptyField()) return@setOnClickListener

            saveButton.isEnabled = false
            addUserToServer()
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rgCheck -> {
                    idTitle.text = "Número do RG"
                    userToSend.doc_type = 1
                }

                R.id.cpfCheck -> {
                    idTitle.text = "Número do CPF"
                    userToSend.doc_type = 2
                }
            }
        }

        gender.setOnCheckedChangeListener { _, checkedId ->
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

        radioGroup.check(R.id.rgCheck)
        gender.check(R.id.dontNeedCheck)

        val userParcelable =
            if (intent.hasExtra(USER_EXTRA)) intent.getParcelableExtra(USER_EXTRA) as Parcelable else null
        checkIfEditMode(userParcelable)

    }

    private fun checkIfEditMode(userParcelable: Parcelable?) {
        if (userParcelable == null) return

        userParcelable?.let {
            setupUser(it)
        }
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

        idImageView.setOnClickListener {
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

        editIdPhotoButton.visibility = View.VISIBLE
        editIdPhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_ID_PHOTO) }

        editResidencePhotoButton.visibility = View.VISIBLE
        editResidencePhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO ) }

        user.profile_picture?.let { profileImageView.loadPath(it) }
        user.doc_picture?.let { idImageView.loadPath(it) }
        user.residence_proof_picture?.let { residenceProofImageView.loadPath(it) }
        user.name?.let { completeNameField.setText(it) }

        when (user.doc_type) {
            1 -> {
                rgCheck.isChecked = true
            }
            2 -> {
                cpfCheck.isChecked = true
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
        saveButton.text = "Salvar alterações"
    }

    private fun addUserToServer() {
        progressBar.visibility = View.VISIBLE
        userToSend.name = completeNameField.text.toString()
        userToSend.address = addressField.text.toString()
        userToSend.doc_number = idNumberField.text.toString().toLong()

        val key = userToSend.id ?: usersReference.push().key!!
//        val key = usersReference.push().key!!
        userToSend.id = key

        usersReference.child(key).setValue(userToSend).addOnSuccessListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this@AddUserActivity, "Operação realizada com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this@AddUserActivity, "Ocorreu algum erro", Toast.LENGTH_SHORT).show()
            saveButton.isEnabled = true
        }
    }

    private fun hasEmptyField(): Boolean {
        return completeNameField.text.isNullOrEmpty() ||
                addressField.text.isNullOrEmpty() ||
                idNumberField.text.isNullOrEmpty()
    }

    private fun getImagePath(imageCode: Int): String? {
        when(imageCode) {
            0 -> {
                return userToSend.profile_picture!!
            }

            1 -> {
                return userToSend.doc_picture!!
            }

            2 -> {
                return userToSend.residence_proof_picture!!
            }
            else -> {
                return null
            }
        }
    }

    private fun uploadImage(whichImageCode: Int) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val mountainsRef = storageRef.child("$ts.jpg")

        val file = Uri.fromFile(File(getImagePath(whichImageCode)))
        val uploadTask = mountainsRef.putFile(file)

        uploadTask.addOnProgressListener {
            progressBar.visibility = View.VISIBLE
            val progress = (it.bytesTransferred / it.totalByteCount) * 100

            Log.d(
                "BPR-ADDUSER",
                "Bytestransfered: ${it.bytesTransferred} Progress: $progress and int ${progress.toInt()}"
            )

            if (progress.toInt() == 100) {
                progressBar.visibility = View.GONE
            }
        }
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "pegou não :(", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {

            mountainsRef.downloadUrl.addOnCompleteListener {
                it.result?.let {
                    updateUserImagePath(whichImageCode, it.toString())
                }
            }
        }
    }

    private fun updateUserImagePath(whichImageCode: Int, newPath: String) {
        when (whichImageCode) {
            0 -> {
                userToSend.profile_picture = newPath
            }
            1 -> {
                userToSend.doc_picture = newPath
            }
            2 -> {
                userToSend.residence_proof_picture = newPath
            }

        }
    }

    private fun dispatchTakePictureIntent(code: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "app.igormatos.botaprarodar.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, code)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            val loadImageView = when (requestCode) {
                REQUEST_PROFILE_PHOTO -> {
                    profilePhotoHasChanged = true
                    userToSend.profile_picture = mCurrentPhotoPath
                    uploadImage(0)
                    profileImageView
                }

                REQUEST_ID_PHOTO -> {
                    idPhotoHasChanged = true
                    userToSend.doc_picture = mCurrentPhotoPath
                    uploadImage(1)
                    idImageView
                }

                REQUEST_RESIDENCE_PHOTO -> {
                    residencePhotoHasChanged = true
                    userToSend.residence_proof_picture = mCurrentPhotoPath
                    uploadImage(2)
                    residenceProofImageView
                }

                else -> profileImageView
            }

            Log.d("BFLW-PICTURE", "Image path $mCurrentPhotoPath")

            loadImageView.loadPath(mCurrentPhotoPath)
        }
    }

    lateinit var mCurrentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
}
