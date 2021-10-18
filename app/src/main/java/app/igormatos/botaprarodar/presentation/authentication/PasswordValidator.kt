package app.igormatos.botaprarodar.presentation.authentication

class PasswordValidator : Validator<String?> {
    override fun validate(value: String?) =
        !value.isNullOrEmpty() && value.length >= 6
}
