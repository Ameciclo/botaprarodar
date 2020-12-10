package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.buildBicycle
import app.igormatos.botaprarodar.domain.model.Bicycle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given BicycleRequestConvert")
internal class BicycleRequestConvertTest {

    val bicycleRequestConverter: BicycleRequestConvert = BicycleRequestConvert()

    @Nested
    @DisplayName("WHEN do convert")
    inner class Convert {
        @Test
        fun `should convert bicycle to bicycleRequest`() {
            val bicycleConverted = bicycleRequestConverter.convert(buildBicycle("Bicycle Converted"))

            assertEquals("Bicycle Converted", bicycleConverted.name)
        }

    }
}