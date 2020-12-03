package app.igormatos.botaprarodar.common.validation.rules

import app.igormatos.botaprarodar.common.exception.BlankCommunityFieldsException
import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.network.Community

class EmptyCommunityInputFieldsRules : RuleValidation<Community> {

    override fun validate(model: Community) {
        when {
            model.name!!.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome\"")
            model.description!!.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Descrição\"")
            model.address!!.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Endereço\"")
            model.org_name!!.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Nome do responsável\"")
            model.org_email!!.isBlank() -> throw BlankCommunityFieldsException("Preencha o campo \"Email do responsável\"")
        }
    }

}