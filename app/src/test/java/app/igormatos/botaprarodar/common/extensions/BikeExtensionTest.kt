package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.bikeWithWithdraws
import org.junit.Test


class BikeExtensionTest {

    @Test
    fun `given a bike with a withdraws list, when call convertToBikeRequest() should return a bikeRequest with a withdraws map`() {

        val bikeRequest = bikeWithWithdraws.convertToBikeRequest()

        assert(bikeRequest.withdraws is Map)
    }

    @Test
    fun `given a bike without a withdraws list, when call convertToBikeRequest() should return a bikeRequest with a null withdraws map`() {

        val bikeRequest = bike.convertToBikeRequest()

        assert(bikeRequest.withdraws.isNullOrEmpty())
    }
}