//package app.igormatos.botaprarodar.screens
//
//import android.content.Context
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//
//class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
//    Worker(appContext, workerParams) {
//
//    override fun doWork(): Result {
//
//        // Get the input
////        val imageUriInput = getInputData().getString(EXTRA_IMAGE_PATH)
//
//        // Do the work
////        val response = uploadFile(imageUriInput!!)
//
//        // Create the output of the work
////        val outputData = workDataOf(EXTRA_IMAGE_PATH to response)
//
//        // Return the output
//        return Result.success()
////        return Result.success(outputData)
//
//    }
//
////    private fun uploadFile(filePath: String): String {
////        val storage = FirebaseStorage.getInstance()
////        val storageRef = storage.reference
////
////        val tsLong = System.currentTimeMillis() / 1000
////        val ts = tsLong.toString()
////        val mountainsRef = storageRef.child("$ts.jpg")
////
////        val file = Uri.fromFile(File(filePath))
////        val uploadTask = mountainsRef.putFile(file)
////
////        var uri = mountainsRef.downloadUrl.result
////        uploadTask.addOnFailureListener {
////        }.addOnSuccessListener {
////
////            mountainsRef.downloadUrl.addOnCompleteListener {
////                it.result?.let {
////                    it.toString()
////                }
////            }
////
////        }
////    }
//}
//
