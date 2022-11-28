package app.igormatos.botaprarodar.common.enumType

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class BicycleReturnUseTypeTest {
    @ParameterizedTest
    @EnumSource(BicycleReturnUseType::class)
    fun `WHEN useBikeTripType is parameterized THEN should return index between 0 and 5`(bicyclereturnUseType: BicycleReturnUseType){
        val index  = bicyclereturnUseType.index
        assert(index in 0..5)
    }
}
