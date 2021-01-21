package app.igormatos.botaprarodar.data.repository

import android.net.Uri
import android.util.Log
import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File
import java.lang.Exception

class FirebaseHelperRepository(private val firebaseStorage: FirebaseStorage) {

    suspend fun uploadImage(
        imagePath: String,
        bikeSerialNumber: String
    ): SimpleResult<ImageUploadResponse> {
        val storageRef = firebaseStorage.reference

        val timeStamp = getCurrentTimeStampMillis()
        val fileReference = getStorageReference(
            storageRef, "community/bike/$bikeSerialNumber _$timeStamp.jpg"
        )
        val thumbReference = getStorageReference(
            storageRef,
            "community/bike/$bikeSerialNumber _thumb_$timeStamp.jpg"
        )

        val fileUri = Uri.fromFile(File(imagePath))
        val uploadTask = fileReference.putFile(fileUri)

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

    private suspend fun getImagePathFromFirebase(storageReference: StorageReference, fileUri: Uri): String {
        return storageReference.putFile(fileUri)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()
    }

    fun getCurrentTimeStampMillis(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }

    fun getStorageReference(storageRef: StorageReference, finalPath: String) =
        storageRef.child(finalPath)

}