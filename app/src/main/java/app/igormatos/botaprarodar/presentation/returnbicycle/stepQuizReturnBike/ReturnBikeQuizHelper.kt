package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import app.igormatos.botaprarodar.R

private const val WORK_REASON = "Seu local de trabalho"
private const val STUDY_REASON = "Seu local de estudo"
private const val RECREATION_REASON = "Seu local de lazer / convivência social"
private const val SHOPPING_REASON = "Seu local de compras"
private const val CHILD_REASON = "Local de estudo da criança"
private const val ANOTHER_REASON = "Outro motivo não especificado"
private const val YES = "Sim"
private const val NO = "Não"
private const val INVALID_RADIO_BUTTON_ID = -1


fun getReasonByRadioButton(radioButtonId: Int): String {
    return when (radioButtonId) {
        R.id.workplaceRb -> WORK_REASON
        R.id.studyRb -> STUDY_REASON
        R.id.entertainmentRb -> RECREATION_REASON
        R.id.shoppingRb -> SHOPPING_REASON
        R.id.childStudyRb -> CHILD_REASON
        else -> ANOTHER_REASON
    }
}

fun getYesOrNoByRadioButton(radioButtonId: Int): String {
    return when (radioButtonId) {
        R.id.problemsDuringRidingYes, R.id.needTakeRideYes -> YES
        else -> NO
    }
}

fun getRadioButtonIdByReason(reason: String): Int {
    return when (reason) {
        WORK_REASON -> R.id.workplaceRb
        STUDY_REASON -> R.id.studyRb
        RECREATION_REASON -> R.id.entertainmentRb
        SHOPPING_REASON -> R.id.shoppingRb
        CHILD_REASON -> R.id.childStudyRb
        ANOTHER_REASON -> R.id.otherRb
        else -> INVALID_RADIO_BUTTON_ID
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