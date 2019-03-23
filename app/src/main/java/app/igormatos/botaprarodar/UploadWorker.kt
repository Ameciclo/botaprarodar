package app.igormatos.botaprarodar

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_user.*
import java.io.File

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Get the input
        val imageUriInput = getInputData().getString(EXTRA_IMAGE_PATH)

        // Do the work
//        val response = uploadFile(imageUriInput!!)

        // Create the output of the work
//        val outputData = workDataOf(EXTRA_IMAGE_PATH to response)

        // Return the output
        return Result.success()
//        return Result.success(outputData)

    }

//    private fun uploadFile(filePath: String): String {
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//
//        val tsLong = System.currentTimeMillis() / 1000
//        val ts = tsLong.toString()
//        val mountainsRef = storageRef.child("$ts.jpg")
//
//        val file = Uri.fromFile(File(filePath))
//        val uploadTask = mountainsRef.putFile(file)
//
//        var uri = mountainsRef.downloadUrl.result
//        uploadTask.addOnFailureListener {
//        }.addOnSuccessListener {
//
//            mountainsRef.downloadUrl.addOnCompleteListener {
//                it.result?.let {
//                    it.toString()
//                }
//            }
//
//        }
//    }
}

