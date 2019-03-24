package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import app.igormatos.botaprarodar.model.Bicycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_bike.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddBikeActivity : AppCompatActivity() {

    var REQUEST_PHOTO = 1
    var bicycleToAdd = Bicycle()
    var bicyclesReference = FirebaseDatabase.getInstance().getReference("bicycles")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bike)

        bikePhotoImageView.setOnClickListener { dispatchTakePictureIntent() }

        saveButton.setOnClickListener {
            if(hasEmptyField()) {
                Toast.makeText(this@AddBikeActivity, "Preencha todos campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveButton.isEnabled = false

            uploadImage { addUserToServer() }
        }

    }

    fun hasEmptyField() : Boolean {
        return orderNumber.text.isNullOrEmpty() ||
                serieNumber.text.isNullOrEmpty() ||
                bikeName.text.isNullOrEmpty() ||
                bikePhotoImageView.drawable == null
    }

    fun addUserToServer() {
        bicycleToAdd.name = bikeName.text.toString()
        bicycleToAdd.serial_number = serieNumber.text.toString()
        bicycleToAdd.order_number = orderNumber.text.toString().toLong()

        val key = bicyclesReference.push().key!!
        bicycleToAdd.id = key
        bicyclesReference.child(key).setValue(bicycleToAdd).addOnSuccessListener {
            Toast.makeText(this@AddBikeActivity, "Bicicleta adicionada com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            saveButton.isEnabled = true
        }
    }

    private fun dispatchTakePictureIntent() {
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
                    startActivityForResult(takePictureIntent, REQUEST_PHOTO)
                }
            }
        }
    }

    lateinit var mCurrentPhotoPath: String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {

            Glide.with(this)
                .load(mCurrentPhotoPath)
                .apply(RequestOptions.fitCenterTransform())
                .into(bikePhotoImageView)

        }
    }

    private fun uploadImage(afterSuccess: () -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val mountainsRef = storageRef.child("$ts.jpg")

        bikePhotoImageView.setDrawingCacheEnabled(true)
        bikePhotoImageView.buildDrawingCache()

        val bitmap = (bikePhotoImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnProgressListener {
            progressBar.visibility = View.VISIBLE
            val progress = (it.bytesTransferred / it.totalByteCount) * 100

            Log.d("BPR-ADDBIKE", "Bytestransfered: ${it.bytesTransferred} Progress: $progress and int ${progress.toInt()}" )

            if (progress.toInt() == 100) {
                progressBar.visibility = View.GONE
            }
        }
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "pegou não :(", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {

            mountainsRef.downloadUrl.addOnCompleteListener {
                it.result?.let {
                    bicycleToAdd.photo_path = it.toString()
                    afterSuccess()
                }
            }
        }
    }

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
