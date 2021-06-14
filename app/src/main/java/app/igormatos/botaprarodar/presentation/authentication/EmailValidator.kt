package app.igormatos.botaprarodar.presentation.authentication

import android.util.Patterns

class EmailValidator : Validator<String?> {
    override fun validate(value: String?) =
        Patterns.EMAIL_ADDRESS.matcher(value.toString()).matches()
}
