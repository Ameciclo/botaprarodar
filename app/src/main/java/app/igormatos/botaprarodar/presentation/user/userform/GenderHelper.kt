package app.igormatos.botaprarodar.presentation.user.userform

import app.igormatos.botaprarodar.R

private const val MALE_ID = 0
private const val FEMALE_ID = 1
private const val OTHER_ID = 2
const val NO_ANSWER = 3

fun getGenderId(radioButtonGenderId: Int): Int {
    return when (radioButtonGenderId) {
        else -> NO_ANSWER
    }
}

fun getRadioButtonId(genderId: Int): Int {
    return when (genderId) {
        else -> 3
    }
}