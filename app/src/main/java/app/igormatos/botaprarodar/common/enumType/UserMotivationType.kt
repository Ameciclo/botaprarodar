package app.igormatos.botaprarodar.common.enumType

enum class UserMotivationType(val index: Int, val value: String) {
    TO_SAVE_MONEY(0, "Para economizar dinheiro, usar bicicleta é mais barato."),
    BECAUSE_IS_MORE_ECOLOGIC(1, "Porque é mais ecológico. A bicicleta não polui o ambiente."),
    TO_SAVE_TIME(2, "Para economizar tempo. Usar a bicicleta como transporte é mais eficiente."),
    TO_BETTER_PHYSICAL_MENTAL_HEALTH(3, "Para melhorar a saúde física e emocional."),
    BECAUSE_START_JOB_DELIVERY(4, "Porque começou a trabalhar com entregas."),
    OTHER(5, "Outro")
}
