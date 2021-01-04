package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.buildBicycle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given BicycleRequestConvert")
internal class BikeRequestConvertTest {

    val bikeRequestConverter: BikeRequestConvert = BikeRequestConvert()

    @Nested
    @DisplayName("WHEN do convert")
    inner class Convert {
        @Test
        fun `should convert bicycle to bicycleRequest`() {
            val bicycleConverted = bikeRequestConverter.convert(buildBicycle("Bicycle Converted"))

            assertEquals("Bicycle Converted", bicycleConverted.name)
        }

    }
}