package app.igormatos.botaprarodar.common.enumType

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.igormatos.botaprarodar.R

enum class StepConfigType(@DrawableRes val icon: Int, @StringRes val title: Int) {
    SELECT_BIKE(R.drawable.ic_bike, R.string.select_bike),
    QUIZ(R.drawable.ic_quiz, R.string.answer_quiz),
    CONFIRM_RETURN(R.drawable.ic_confirm, R.string.confirm_return),
    EMPRESTA(R.drawable.ic_confirm, R.string.confirm_return)
}