package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User

object Fixtures {

    val adminUser = Admin(email = "admin@admin.com", password = "admin", id = "admin001")

    val bike = Bike().apply {
        name = "name mock"
        orderNumber = 123
        serialNumber = "serial mock"
        photoPath = "photo mock"
        isAvailable = true
    }

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
        isBlocked = true
        profilePicture = "https://profile.jpeg"
        profilePictureThumbnail = "https://thumb.jpeg"
        residenceProofPicture = "https://residence.jpeg"
        racial = "Branca"
        schooling = "Ensino superior completo"
        schoolingStatus = "Completo"
        income = "0"
        age = "01/01/2000"
        telephone = "113975-4562"
    }
}