package app.igormatos.botaprarodar.domain.model.user

import androidx.annotation.IntDef

@IntDef(
    Gender.MALE,
    Gender.FEMALE,
    Gender.OTHER,
    Gender.DONT_NEED
)
annotation class Gender {
    companion object {
        const val MALE = 0
        const val FEMALE = 1
        const val OTHER = 2
        const val DONT_NEED = 3
    }
}