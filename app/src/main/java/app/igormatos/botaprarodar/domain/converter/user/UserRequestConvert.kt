package app.igormatos.botaprarodar.domain.converter.user

import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.transformNullToEmpty

class UserRequestConvert : Converter<User, UserRequest> {
    override fun convert(toConvert: User): UserRequest {
        return buildUserRequest(toConvert)
    }

    private fun buildUserRequest(user: User) =
        UserRequest(
            name = user.name.transformNullToEmpty(),
            createdDate = user.createdDate.transformNullToEmpty(),
            address = user.address.transformNullToEmpty(),
            available = user.isAvailable,
            blocked = user.isBlocked,
            gender = user.gender.transformNullToEmpty(),
            residenceProofPicture = user.residenceProofPicture.transformNullToEmpty(),
            docNumber = user.docNumber,
            docPicture = user.docPicture.transformNullToEmpty(),
            docPictureBack = user.docPictureBack.transformNullToEmpty(),
            docType = user.docType,
            profilePicture = user.profilePicture.transformNullToEmpty(),
            profilePictureThumbnail = user.profilePictureThumbnail.transformNullToEmpty(),
            id = user.id.transformNullToEmpty()
        )
}