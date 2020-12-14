package app.igormatos.botaprarodar.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import app.igormatos.botaprarodar.presentation.adduser.AddUserWrapper
import com.brunotmgomes.ui.extensions.createImageFile
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