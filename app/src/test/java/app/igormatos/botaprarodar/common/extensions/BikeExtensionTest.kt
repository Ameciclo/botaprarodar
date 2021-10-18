package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.bikeWithWithdraws
import app.igormatos.botaprarodar.utils.devolution
import app.igormatos.botaprarodar.utils.withdraw
import junit.framework.Assert.assertTrue
import org.junit.Test

class BikeExtensionTest {

    @Test
    fun `given a bike with a withdraws list, when call convertToBikeRequest() should return a bikeRequest with a withdraws map`() {
        val bikeRequest = bikeWithWithdraws.convertToBikeRequest()

        assert(bikeRequest.withdraws is Map)
        assertTrue(bikeRequest.withdraws?.get("123") == withdraw)
    }

    @Test
    fun `given a bike without a withdraws list, when call convertToBikeRequest() should return a bikeRequest with a null withdraws map`() {
        val bikeRequest = bike.convertToBikeRequest()

        assert(bikeRequest.withdraws.isNullOrEmpty())
    }

    @Test
    fun `given a bike with a devolution list, when call convertToBikeRequest() should return a bikeRequest with a devolutions map`() {
        val bikeRequest = bikeWithWithdraws.convertToBikeRequest()

        assert(bikeRequest.devolutions is Map)
        assertTrue(bikeRequest.devolutions?.get("098") == devolution)
    }

    @Test
    fun `given a bike without a devolution list, when call convertToBikeRequest() should return a bikeRequest with a null devolutions map`() {
        val bikeRequest = bike.convertToBikeRequest()

        assert(bikeRequest.devolutions.isNullOrEmpty())
    }
}