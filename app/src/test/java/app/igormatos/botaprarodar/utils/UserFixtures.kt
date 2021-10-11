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
    gender = "Outro"
    docType = 1
    isAvailable = true
    profilePicture = "https://profile.jpeg"
    profilePictureThumbnail = "https://thumb.jpeg"
    residenceProofPicture = "https://residence.jpeg"
    racial = "Branca"
    schooling = "Ensino superior completo"
    schoolingStatus = "Completo"
    income = "0"
    age = "xx"
}

val unavailableUserFake = User().apply {
    id = "testId"
    name = "Capitão América"
    docNumber = 1234567890
    docPictureBack = "https://docback.jpeg"
    docPicture = "https://doc.jpeg"
    address = "Polo Norte - 433La 092Lg"
    createdDate = "01/01/2021"
    gender = "Outro"
    docType = 1
    isAvailable = false
    profilePicture = "https://profile.jpeg"
    profilePictureThumbnail = "https://thumb.jpeg"
    residenceProofPicture = "https://residence.jpeg"
    racial = "Branca"
    schooling = "Ensino superior completo"
    schoolingStatus = "Completo"
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
    gender = "Outro",
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

val racialOptions = listOf<String>(
    "Amarela", "Branca",
    "Indígena",
    "Parda",
    "Preta",
    "Outra/Não deseja informar"
)

val incomeOptions = listOf<String>(
    "Até 150 reais",
    "Entre 150 e 350",
    "Entre 350 e 500",
    "Entre 500 e 750",
    "Entre 750 e 1100",
    "Entre 1100 e 2000",
    "Mais de 2000",
    "Desejo não informar"
)

val schoolingOptions = listOf<String>(
    "Sem instrução ou menos de 1 ano de estudo",
    "Ensino fundamental incompleto",
    "Ensino fundamental completo",
    "Ensino médio incompleto",
    "Ensino médio completo",
    "Ensino técnico incompleto",
    "Ensino técnico completo",
    "Ensino superior incompleto",
    "Ensino superior completo",
    "Não determinado"
)

val genderOptions = listOf<String>(
    "Masculino",
    "Feminino",
    "Outro",
    "Prefere não responder"
)

val schoolingStatusOptions = listOf<String>(
    "Completo",
    "Incompleto",
    "Cursando"
)

fun buildListAvailableUsers(howMuch: Int): List<User> {
    val list = mutableListOf<User>()
    for (i in howMuch downTo 1) {
        list.add(validUser)
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
        map[i.toString()] = validUser
    }
    return map
}

val userException = Exception()

val userFlowSuccess = flowOf(SimpleResult.Success(listUsers))

val userFlowError = flowOf(SimpleResult.Error(userException))

val userSimpleSuccess = SimpleResult.Success(AddDataResponse("User registered"))

val userSimpleSuccessEdit = SimpleResult.Success(AddDataResponse("User edited"))