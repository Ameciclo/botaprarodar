package app.igormatos.botaprarodar.common.validation.rules

import app.igormatos.botaprarodar.common.exception.BlankFieldsException
import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest

class EmptyCommunityInputFieldsRules : RuleValidation<CommunityRequest> {

    override fun validate(model: CommunityRequest) {
        when {
            model.name.isNullOrBlank() -> throw BlankFieldsException("Preencha o campo \"Nome\"")
            model.description.isNullOrBlank() -> throw BlankFieldsException("Preencha o campo \"Descrição\"")
            model.address.isNullOrBlank() -> throw BlankFieldsException("Preencha o campo \"Endereço\"")
            model.orgName.isNullOrBlank() -> throw BlankFieldsException("Preencha o campo \"Nome do responsável\"")
            model.orgEmail.isNullOrBlank() -> throw BlankFieldsException("Preencha o campo \"Email do responsável\"")
        }
    }

}