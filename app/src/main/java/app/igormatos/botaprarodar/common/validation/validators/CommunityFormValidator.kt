package app.igormatos.botaprarodar.common.validation.validators

import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.common.validation.Validator
import app.igormatos.botaprarodar.common.validation.rules.EmptyCommunityInputFieldsRules
import app.igormatos.botaprarodar.network.Community

class CommunityFormValidator(
    private val emptyCommunityInputFieldsRules: EmptyCommunityInputFieldsRules
) : Validator<Community> {

    override fun getRules(): List<RuleValidation<Community>> = listOf(emptyCommunityInputFieldsRules)

}