package app.igormatos.botaprarodar.common.validation

interface Validator<T> {

    fun validate(entity: T) {
        for (ruleValidantion in getRules()) {
            ruleValidantion.validate(entity)
        }
    }

    fun getRules(): List<RuleValidation<T>>

}