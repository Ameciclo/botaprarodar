package app.igormatos.botaprarodar.common.validation.validators

import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.common.validation.Validator
import app.igormatos.botaprarodar.common.validation.rules.EmptyCommunityInputFieldsRules
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest

class CommunityFormValidator(
    private val emptyCommunityInputFieldsRules: EmptyCommunityInputFieldsRules
) : Validator<CommunityRequest> {

    override fun getRules(): List<RuleValidation<CommunityRequest>> = listOf(emptyCommunityInputFieldsRules)

}