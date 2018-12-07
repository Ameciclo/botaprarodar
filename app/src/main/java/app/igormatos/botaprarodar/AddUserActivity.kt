package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_user.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import app.igormatos.botaprarodar.model.User
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddUserActivity : AppCompatActivity() {

    val REQUEST_FILE = 2
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1

    val REQUEST_PROFILE_PHOTO = 1
    val REQUEST_ID_PHOTO = 2
    val REQUEST_RESIDENCE_PHOTO = 3

    val CODE_EXTRA = "CODE_EXTRA"
    val FILE_NAME_EXTRA = "FILE_NAME_EXTRA"

    lateinit var bitmapToUpload: Bitmap

    var userToSend = User()
    var usersReference = FirebaseDatabase.getInstance().getReference("users")

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

            uploadImage { addUserToServer() }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
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

        radioGroup.check(R.id.rgCheck)

//        residenceProofField.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "file/*"
//            intent.putExtra("CONTENT_TYPE", "*/*")
//            startActivityForResult(intent, REQUEST_FILE)
//        }
    }

    private fun addUserToServer() {
        userToSend.name = completeNameField.text.toString()
        userToSend.address = addressField.text.toString()
        userToSend.doc_number = idNumberField.text.toString().toLong()

        val key = usersReference.push().key!!
        userToSend.id = key
        usersReference.child(key).setValue(userToSend).addOnSuccessListener {
            Toast.makeText(this@AddUserActivity, "Usuário adicionado com sucesso", Toast.LENGTH_SHORT).show()
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

    private fun uploadImage(afterSuccess: () -> Unit) {
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
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
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

            Glide.with(this)
                .load(mCurrentPhotoPath)
                .apply(RequestOptions.fitCenterTransform())
                .into(loadImageView)

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
