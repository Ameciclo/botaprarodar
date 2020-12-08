package app.igormatos.botaprarodar.presentation.adduser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import com.brunotmgomes.ui.extensions.createImageFile
import com.brunotmgomes.ui.extensions.takePictureIntent
import java.io.File
import java.io.IOException

class TakePicture : ActivityResultContract<Int, AddUserWrapper>() {

    private var photoFile: File? = null
    private var requestCode: Int? = null

    override fun createIntent(context: Context, input: Int?): Intent {
        this.requestCode = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->

            photoFile = try {
                context.createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "app.igormatos.botaprarodar.provider",
                    it
                )
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AddUserWrapper? {
        if (resultCode != Activity.RESULT_OK) return null
        return AddUserWrapper(photoFile?.absolutePath, requestCode)
    }
}