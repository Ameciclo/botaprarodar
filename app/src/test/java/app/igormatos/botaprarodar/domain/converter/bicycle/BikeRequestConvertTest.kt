package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.getBikeFixture
import app.igormatos.botaprarodar.domain.model.Bike
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
            var bike =  getBikeFixture("Bicycle Converted")
            val bicycleConverted = bikeRequestConverter.convert(bike)

            assertEquals("Bicycle Converted", bicycleConverted.name)
        }

        @Test
        fun ` When bike name is null then bicycleRequest name should be empty`() {
            var bike =  Bike()
            val bicycleConverted = bikeRequestConverter.convert(bike)

            assertEquals("", bicycleConverted.name)
        }

    }
}