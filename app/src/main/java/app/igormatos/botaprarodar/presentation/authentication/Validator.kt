package app.igormatos.botaprarodar.presentation.authentication

interface Validator<T> {
    fun validate(value: T): Boolean
}