package app.igormatos.botaprarodar.common.validation.validators

import app.igormatos.botaprarodar.common.validation.RuleValidation
import app.igormatos.botaprarodar.common.validation.Validator
import app.igormatos.botaprarodar.common.validation.rules.EmptyCommunityInputFieldsRules
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityBody

class CommunityFormValidator(
    private val emptyCommunityInputFieldsRules: EmptyCommunityInputFieldsRules
) : Validator<CommunityBody> {

    override fun getRules(): List<RuleValidation<CommunityBody>> = listOf(emptyCommunityInputFieldsRules)

}