package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import app.igormatos.botaprarodar.R

private const val YES = "Sim"
private const val NO = "Não"
private const val INVALID_RADIO_BUTTON_ID = -1


fun getYesOrNoByRadioButton(radioButtonId: Int): String {
    return when (radioButtonId) {
        R.id.problemsDuringRidingYes, R.id.needTakeRideYes -> YES
        else -> NO
    }
}

fun getRadioButtonIdBySufferedViolence(sufferedViolence: String): Int {
    return when (sufferedViolence) {
        YES -> R.id.problemsDuringRidingYes
        NO -> R.id.problemsDuringRidingNo
        else -> INVALID_RADIO_BUTTON_ID
    }
}

fun getRadioButtonIdByGiveRide(giveRide: String): Int {
    return when (giveRide) {
        YES -> R.id.needTakeRideYes
        NO -> R.id.needTakeRideNo
        else -> INVALID_RADIO_BUTTON_ID
    }
}