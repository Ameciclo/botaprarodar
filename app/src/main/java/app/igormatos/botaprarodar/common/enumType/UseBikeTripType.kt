package app.igormatos.botaprarodar.common.enumType

enum class UseBikeTripType(val index: Int, val value: String) {
    DELIVERY_BY_APPS(0, "Para realizar entregas de aplicativos."),
    GO_TO_WORKPLACE(1, "Deslocar para o local de trabalho."),
    GO_TO_STUDY_PLACE(2, "Deslocar para o local de estudo."),
    TAKE_CHILDREN_SCHOOL(3, "Levar crianças para escola ou creche."),
    SOLVE_THINGS_DAY_OF_DAY(4, "Resolver coisas do dia a dia. Ex: Mercado, lotéricas, banco."),
    LEISURE_WALK(5, "Para passear, lazer.");

    companion object {
        fun getUserMotivationTypeByIndex(index: Int): UseBikeTripType? {
            return when (index) {
                DELIVERY_BY_APPS.index -> DELIVERY_BY_APPS
                GO_TO_WORKPLACE.index -> GO_TO_WORKPLACE
                GO_TO_STUDY_PLACE.index -> GO_TO_STUDY_PLACE
                TAKE_CHILDREN_SCHOOL.index -> TAKE_CHILDREN_SCHOOL
                SOLVE_THINGS_DAY_OF_DAY.index -> SOLVE_THINGS_DAY_OF_DAY
                LEISURE_WALK.index -> LEISURE_WALK
                else -> null
            }
        }

        fun getUserMotivationTypeByValue(value: String): UseBikeTripType? {
            return when (value) {
                DELIVERY_BY_APPS.value -> DELIVERY_BY_APPS
                GO_TO_WORKPLACE.value -> GO_TO_WORKPLACE
                GO_TO_STUDY_PLACE.value -> GO_TO_STUDY_PLACE
                TAKE_CHILDREN_SCHOOL.value -> TAKE_CHILDREN_SCHOOL
                SOLVE_THINGS_DAY_OF_DAY.value -> SOLVE_THINGS_DAY_OF_DAY
                LEISURE_WALK.value -> LEISURE_WALK
                else -> null
            }
        }
    }

}
