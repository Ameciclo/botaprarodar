package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf

val validUser = User().apply {
    id = "testId"
    name = "Capitão América"
    docNumber = 1234567890
    docPictureBack = "https://docback.jpeg"
    docPicture = "https://doc.jpeg"
    address = "Polo Norte - 433La 092Lg"
    createdDate = "01/01/2021"
    gender = 0
    docType = 1
    isAvailable = true
    profilePicture = "https://profile.jpeg"
    profilePictureThumbnail = "https://thumb.jpeg"
    residenceProofPicture = "https://residence.jpeg"
}

val unavailableUserFake = User().apply {
    id = "testId"
    name = "Capitão América"
    docNumber = 1234567890
    docPictureBack = "https://docback.jpeg"
    docPicture = "https://doc.jpeg"
    address = "Polo Norte - 433La 092Lg"
    createdDate = "01/01/2021"
    gender = 0
    docType = 1
    isAvailable = false
    profilePicture = "https://profile.jpeg"
    profilePictureThumbnail = "https://thumb.jpeg"
    residenceProofPicture = "https://residence.jpeg"
    racial = "Branca"
    schooling = "Ensino superior completo"
    income = "0"
    age = "xx"
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

val listUsers = mutableListOf(validUser)

fun buildListAvailableUsers(howMuch: Int): List<User> {
    val list = mutableListOf<User>()
    for (i in howMuch downTo 1) {
        list.add(userFake)
    }
    return list
}

fun buildListUnavailableUsers(howMuch: Int): List<User> {
    val list = mutableListOf<User>()
    for (i in howMuch downTo 1) {
        list.add(unavailableUserFake)
    }
    return list
}

fun buildMapStringUser(howMuch: Int): Map<String, User> {
    val map = mutableMapOf<String, User>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = userFake
    }
    return map
}

val userException = Exception()

val userFlowSuccess = flowOf(SimpleResult.Success(listUsers))

val userFlowError = flowOf(SimpleResult.Error(userException))

val userSimpleSuccess = SimpleResult.Success(AddDataResponse("User registered"))

val userSimpleSuccessEdit = SimpleResult.Success(AddDataResponse("User edited"))