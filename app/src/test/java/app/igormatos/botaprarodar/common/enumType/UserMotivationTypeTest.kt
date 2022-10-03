package app.igormatos.botaprarodar.common.enumType

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


class UserMotivationTypeTest {

    @ParameterizedTest
    @EnumSource(UserMotivationType::class)
    fun `WHEN userMotivationType is parameterized THEN should return index between 0 and 5`(userMotivationType: UserMotivationType){
        val index  = userMotivationType.index
        assert(index in 0..5)
    }
}
