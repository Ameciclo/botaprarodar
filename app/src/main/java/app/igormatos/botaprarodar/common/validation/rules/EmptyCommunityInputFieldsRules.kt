package app.igormatos.botaprarodar.common.validation.rules

import app.igormatos.botaprarodar.common.exception.BlankCommunityFieldsException
import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest

class EmptyCommunityInputFieldsRules : RuleValidation<CommunityRequest> {

    override fun validate(model: CommunityRequest) {
        when {
            model.name.isNullOrBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome\"")
            model.description.isNullOrBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Descrição\"")
            model.address.isNullOrBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Endereço\"")
            model.orgName.isNullOrBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome do responsável\"")
            model.orgEmail.isNullOrBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Email do responsável\"")
        }
    }

}