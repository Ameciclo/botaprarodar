package app.igormatos.botaprarodar.common.enumType

enum class UserMotivationType(val index: Int, val value: String) {
    TO_SAVE_MONEY(0, "Para economizar dinheiro. Usar bicicleta é mais barato."),
    BECAUSE_IS_MORE_ECOLOGIC(1, "Porque é mais ecológico. A bicicleta não polui o ambiente."),
    TO_SAVE_TIME(2, "Para economizar tempo. Usar a bicicleta como transporte é mais eficiente."),
    TO_BETTER_PHYSICAL_MENTAL_HEALTH(3, "Para melhorar a saúde física e emocional."),
    BECAUSE_START_JOB_DELIVERY(4, "Porque começou a trabalhar com entregas."),
    OTHER(5, "Outro.");

    companion object {
        fun getUserMotivationTypeByIndex(index: Int): UserMotivationType? {
            return when (index) {
                TO_SAVE_MONEY.index -> TO_SAVE_MONEY
                BECAUSE_IS_MORE_ECOLOGIC.index -> BECAUSE_IS_MORE_ECOLOGIC
                TO_SAVE_TIME.index -> TO_SAVE_TIME
                TO_BETTER_PHYSICAL_MENTAL_HEALTH.index -> TO_BETTER_PHYSICAL_MENTAL_HEALTH
                BECAUSE_START_JOB_DELIVERY.index -> BECAUSE_START_JOB_DELIVERY
                OTHER.index -> OTHER
                else -> null
            }
        }

        fun getUserMotivationTypeByValue(value: String): UserMotivationType? {
            return when (value) {
                TO_SAVE_MONEY.value -> TO_SAVE_MONEY
                BECAUSE_IS_MORE_ECOLOGIC.value -> BECAUSE_IS_MORE_ECOLOGIC
                TO_SAVE_TIME.value -> TO_SAVE_TIME
                TO_BETTER_PHYSICAL_MENTAL_HEALTH.value -> TO_BETTER_PHYSICAL_MENTAL_HEALTH
                BECAUSE_START_JOB_DELIVERY.value -> BECAUSE_START_JOB_DELIVERY
                OTHER.value -> OTHER
                else -> null
            }
        }
    }

}
