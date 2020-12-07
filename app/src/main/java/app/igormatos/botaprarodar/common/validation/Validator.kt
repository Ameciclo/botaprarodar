package app.igormatos.botaprarodar.common.validation

import app.igormatos.botaprarodar.domain.model.community.CommunityBody

interface Validator<T> {

    fun validate(entity: T) {
        for (ruleValidantion in getRules()) {
            ruleValidantion.validate(entity)
        }
    }

    fun getRules(): List<RuleValidation<T>>

}