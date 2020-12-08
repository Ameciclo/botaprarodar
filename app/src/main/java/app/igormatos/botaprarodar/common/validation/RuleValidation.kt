package app.igormatos.botaprarodar.common.validation

interface RuleValidation<T> {

    fun validate(model: T)

}