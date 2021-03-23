package app.igormatos.botaprarodar.domain.adapter

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import kotlinx.coroutines.flow.MutableStateFlow

interface StepperAdapter {
    val steps: List<StepConfigType>
    val currentStep: MutableStateFlow<StepConfigType>
    fun navigateToNext()
    fun navigateToPrevious()
    fun setCurrentStep(currentStep: StepConfigType)
    fun navigateToInitialStep()
}
