package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.domain.model.User

val userFake = User().apply {
    name = "Capitão América"
    doc_number = 1234567890
    doc_picture_back = "https://docback.jpeg"
    doc_picture = "https://doc.jpeg"
    address = "Polo Norte - 433La 092Lg"
    birthday = "01/01/1900"
    created_date = "01/01/2021"
    gender = 0
    doc_type = 1
    profile_picture = "https://profile.jpeg"
    profile_picture_thumbnail = "https://thumb.jpeg"
    residence_proof_picture = "https://residence.jpeg"
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