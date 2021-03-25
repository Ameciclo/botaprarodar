package app.igormatos.botaprarodar.presentation.user.userform

import app.igormatos.botaprarodar.R

private const val MALE_ID = 0
private const val FEMALE_ID = 1
private const val OTHER_ID = 2
const val NO_ANSWER = 3

fun getGenderId(radioButtonGenderId: Int): Int {
    return when (radioButtonGenderId) {
        R.id.rbGenderMale -> MALE_ID
        R.id.rbGenderFemale -> FEMALE_ID
        R.id.rbGenderOther -> OTHER_ID
        else -> NO_ANSWER
    }
}

fun getRadioButtonId(genderId: Int): Int {
    return when (genderId) {
        MALE_ID -> R.id.rbGenderMale
        FEMALE_ID -> R.id.rbGenderFemale
        OTHER_ID -> R.id.rbGenderOther
        else -> R.id.rbGenderNoAnswer
    }
}