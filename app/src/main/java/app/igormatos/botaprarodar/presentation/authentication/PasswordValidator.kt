package app.igormatos.botaprarodar.presentation.authentication

class PasswordValidator : Validator<String> {
    override fun validate(value: String) =
        value.isNotEmpty() && value.length >= 6
}
