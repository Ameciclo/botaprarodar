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
            createdDate = user.created_date.transformNullToEmpty(),
            address = user.address.transformNullToEmpty(),
            available = user.isAvailable,
            gender = user.gender,
            residenceProofPicture = user.residence_proof_picture.transformNullToEmpty(),
            docNumber = user.doc_number,
            docPicture = user.doc_picture.transformNullToEmpty(),
            docPictureBack = user.doc_picture_back.transformNullToEmpty(),
            docType = user.doc_type,
            profilePicture = user.profile_picture.transformNullToEmpty(),
            profilePictureThumbnail = user.profile_picture_thumbnail.transformNullToEmpty()
        )
}