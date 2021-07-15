package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf

val userFake = User().apply {
    id = "testId"
    name = "Capitão América"
    docNumber = 1234567890
    docPictureBack = "https://docback.jpeg"
    docPicture = "https://doc.jpeg"
    address = "Polo Norte - 433La 092Lg"
    createdDate = "01/01/2021"
    gender = 0
    docType = 1
    profilePicture = "https://profile.jpeg"
    profilePictureThumbnail = "https://thumb.jpeg"
    residenceProofPicture = "https://residence.jpeg"
}

val userRequest = UserRequest(
    name = "mock",
    createdDate = "",
    id = "",
    address = "",
    docNumber = 0,
    docPicture = "",
    docPictureBack = "",
    docType = 0,
    gender = 0,
    path = "",
    profilePicture = "",
    profilePictureThumbnail = "",
    residenceProofPicture = ""
)

val mockImageUploadResponse = ImageUploadResponse(
    fullImagePath = "teste",
    thumbPath = "teste"
)

val listUsers = mutableListOf(userFake)

val userException = Exception()

val userFlowSuccess = flowOf(SimpleResult.Success(listUsers))

val userFlowError = flowOf(SimpleResult.Error(userException))

val userSimpleSuccess = SimpleResult.Success(AddDataResponse("User registered"))

val userSimpleSuccessEdit = SimpleResult.Success(AddDataResponse("User edited"))