package app.igormatos.botaprarodar.presentation.authentication

class EmailValidator : Validator<String> {
    override fun validate(value: String) =
        value.isNotEmpty() && value.trim().contains("@")
}
