package app.igormatos.botaprarodar.presentation.returnbicycle.quiz

import app.igormatos.botaprarodar.R

private const val WORK_REASON = "Seu local de trabalho"
private const val STUDY_REASON = "Seu local de estudo"
private const val RECREATION_REASON = "Seu local de lazer / convivência social"
const val SHOPPING_REASON = "Seu local de compras"
const val CHILD_REASON = "Local de estudo da criança"
const val ANOTHER_REASON = "Outro motivo não especificado"
const val YES = "Sim"
const val NO = "Não"

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
        else -> R.id.otherRb
    }
}

fun getRadioButtonIdBySufferedViolence(sufferedViolence: String): Int {
    return when (sufferedViolence) {
        YES -> R.id.problemsDuringRidingYes
        else -> R.id.problemsDuringRidingNo
    }
}

fun getRadioButtonIdByGiveRide(giveRide: String): Int {
    return when (giveRide) {
        YES -> R.id.needTakeRideYes
        else -> R.id.needTakeRideNo
    }
}