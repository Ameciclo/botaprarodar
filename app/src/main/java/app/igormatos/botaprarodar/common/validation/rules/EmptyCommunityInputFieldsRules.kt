package app.igormatos.botaprarodar.common.validation.rules

import app.igormatos.botaprarodar.common.exception.BlankCommunityFieldsException
import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.domain.model.community.CommunityBody

class EmptyCommunityInputFieldsRules : RuleValidation<CommunityBody> {

    override fun validate(model: CommunityBody) {
        when {
            model.name.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome\"")
            model.description.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Descrição\"")
            model.address.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Endereço\"")
            model.orgName.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome do responsável\"")
            model.orgEmail.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Email do responsável\"")
        }
    }

}