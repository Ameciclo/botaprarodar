package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import java.io.ByteArrayOutputStream
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
            if(hasEmptyField()) return@setOnClickListener

            saveButton.isEnabled = false

            uploadImageFromImageView { addUserToServer() }
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
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
            when(checkedId) {
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


        val userParcelable = if (intent.hasExtra(USER_EXTRA)) intent.getParcelableExtra(USER_EXTRA) as Parcelable else null
        checkIfEditMode(userParcelable)

//        residenceProofField.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "file/*"
//            intent.putExtra("CONTENT_TYPE", "*/*")
//            startActivityForResult(intent, REQUEST_FILE)
//        }

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
            fullscreenIntent.putExtra(EXTRA_IMAGE_PATH,  user.profile_picture)
            startActivity(fullscreenIntent)
        }

        editProfilePhotoButton.setOnClickListener { dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO)  }

        user.profile_picture?.let { profileImageView.loadPath(it) }
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
            1 -> {
                maleCheck.isChecked = true
            }
            2 -> {
                femaleCheck.isChecked = true
            }
            3 -> {
                otherCheck.isChecked = true
            }
            4 -> {
                dontNeedCheck.isChecked = true
            }
        }


        idNumberField.setText(user.doc_number.toString())


        user.address?.let { addressField.setText(it) }
        saveButton.text = "Salvar alterações"
    }

    private fun addUserToServer() {
        userToSend.name = completeNameField.text.toString()
        userToSend.address = addressField.text.toString()
        userToSend.doc_number = idNumberField.text.toString().toLong()

        val key = userToSend.id ?: usersReference.push().key!!
//        val key = usersReference.push().key!!
        userToSend.id = key

        usersReference.child(key).setValue(userToSend).addOnSuccessListener {
            Toast.makeText(this@AddUserActivity, "Operação realizada com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            saveButton.isEnabled = true
        }
    }

    private fun hasEmptyField() : Boolean {
        return completeNameField.text.isNullOrEmpty() ||
            addressField.text.isNullOrEmpty() ||
            idNumberField.text.isNullOrEmpty()
    }

    private fun uploadImageFromImageView(afterSuccess: () -> Unit) {
        if (!profilePhotoHasChanged) {
            afterSuccess()
            return
        }

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val mountainsRef = storageRef.child("$ts.jpg")

        profileImageView.setDrawingCacheEnabled(true)
        profileImageView.buildDrawingCache()

        val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnProgressListener {
            profileProgressBar.visibility = View.VISIBLE
            val progress = (it.bytesTransferred / it.totalByteCount) * 100

            Log.d("BPR-ADDUSER", "Bytestransfered: ${it.bytesTransferred} Progress: $progress and int ${progress.toInt()}" )
//            profileProgressBar.

            if (progress.toInt() == 100) {
                profileProgressBar.visibility = View.GONE
//                profileProgressBar.progress = 0
            }
        }
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "pegou não :(", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {

            mountainsRef.downloadUrl.addOnCompleteListener {
                it.result?.let {
                    userToSend.profile_picture = it.toString()
                    afterSuccess()
                }
            }
            // taskSnapshot.getMetadata() contains file metada  ta such as size, content-type, etc.
            // ...
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
//                    takePictureIntent.putExtra(CODE_EXTRA, code)
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
                    profileImageView
                }

                REQUEST_ID_PHOTO -> {
                    idImageView
                }

                REQUEST_RESIDENCE_PHOTO -> {
                    residenceProofImageView
                }

                else -> profileImageView
            }

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
