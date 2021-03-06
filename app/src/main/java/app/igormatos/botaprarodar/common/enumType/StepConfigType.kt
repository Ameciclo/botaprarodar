package app.igormatos.botaprarodar.common.enumType

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.igormatos.botaprarodar.R

enum class StepConfigType(@DrawableRes val icon: Int, @StringRes val title: Int) {
    SELECT_BIKE(R.drawable.ic_bike, R.string.select_bike),
    QUIZ(R.drawable.ic_quiz, R.string.answer_quiz),
    CONFIRM_DEVOLUTION(R.drawable.ic_confirm, R.string.confirm_return),
    CONFIRM_WITHDRAW(R.drawable.ic_confirm, R.string.confirm_withdraw),
    SELECT_USER(R.drawable.ic_directions_bike, R.string.select_user),
    USER_FORM(R.drawable.ic_person, R.string.user_form_step_label),
    USER_QUIZ(R.drawable.ic_confirm, R.string.answer_quiz),
}