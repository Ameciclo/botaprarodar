package app.igormatos.botaprarodar.data.repository

import android.net.Uri
import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

class FirebaseHelperRepository(private val firebaseStorage: FirebaseStorage) {

    suspend fun uploadImage(
        imagePath: String,
        finalPath: String
    ): SimpleResult<ImageUploadResponse> {
        val storageRef = firebaseStorage.reference
        val timeStamp = getCurrentTimeStampMillis()
        val fileUri = Uri.fromFile(File(imagePath))

        val fileReference = getStorageReference(
            storageRef,
            "$finalPath _$timeStamp.jpg"
        )
        val thumbReference = getStorageReference(
            storageRef,
            "$finalPath _thumb_$timeStamp.jpg"
        )

        return uploadFileImage(fileUri, fileReference, thumbReference)
    }

    private suspend fun uploadFileImage(
        fileUri: Uri,
        fileReference: StorageReference,
        thumbReference: StorageReference
    ): SimpleResult<ImageUploadResponse> {

        return try {
            val fullImagePath = getImagePathFromFirebase(fileReference, fileUri)
            val thumbPath = getImagePathFromFirebase(thumbReference, fileUri)

            SimpleResult.Success(ImageUploadResponse(fullImagePath, thumbPath))
        } catch (e: Exception) {
            SimpleResult.Error(e)
        }
    }

    private suspend fun getImagePathFromFirebase(storageReference: StorageReference, fileUri: Uri) =
        storageReference.putFile(fileUri)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()

    private fun getCurrentTimeStampMillis() = (System.currentTimeMillis() / 1000).toString()

    private fun getStorageReference(storageRef: StorageReference, finalPath: String) =
        storageRef.child(finalPath)
}

//community/bike/$bikeSerialNumber
//community/bike/$bikeSerialNumber