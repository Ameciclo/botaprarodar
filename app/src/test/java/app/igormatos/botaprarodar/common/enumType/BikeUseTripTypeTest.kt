package app.igormatos.botaprarodar.common.enumType

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class BikeUseTripTypeTest {
    @ParameterizedTest
    @EnumSource(BikeUseTripType::class)
    fun `WHEN useBikeTripType is parameterized THEN should return index between 0 and 5`(bikeUseTripType: BikeUseTripType){
        val index  = bikeUseTripType.index
        assert(index in 0..5)
    }
}
