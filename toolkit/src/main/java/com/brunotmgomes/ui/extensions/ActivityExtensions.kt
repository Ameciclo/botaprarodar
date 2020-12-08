package com.brunotmgomes.ui.extensions

// Code by Igor Matos adapted from https://developer.android.com/training/camera/photobasics

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException


const val REQUEST_PHOTO = 1

fun Activity.takePictureIntent(requestCode: Int, block: (String) -> Unit) {
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
                block(it.absolutePath)

                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "app.igormatos.botaprarodar.provider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }
}