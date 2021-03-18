package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.utils.listBikeRequest
import app.igormatos.botaprarodar.utils.listBikeRequestWithdrawsDevolutions
import junit.framework.Assert.assertTrue
import org.junit.Test

class BikeRequestExtensionTest {

    @Test
    fun `given a bikeRequest list, when call convertToBikeList() should return a list of Bikes`() {
        val bikeList = listBikeRequest.convertToBikeList()

        assertTrue(bikeList.size == 1)
    }

    @Test
    fun `given a bikeRequest list without withdraws and devolutions, when call convertToBikeList() should return a list of Bikes with null withdraws and devolutions`() {
        val bikeList = listBikeRequest.convertToBikeList()

        assertTrue(bikeList.size == 1)
        assertTrue(bikeList[0].withdraws.isNullOrEmpty())
        assertTrue(bikeList[0].devolutions.isNullOrEmpty())
    }

    @Test
    fun `given a bikeRequest list with withdraws and devolutions, when call convertToBikeList() should return a list of Bikes with a list of withdraws and devolutions`() {
        val bikeList = listBikeRequestWithdrawsDevolutions.convertToBikeList()

        assertTrue(bikeList.size == 1)
        assertTrue(bikeList[0].withdraws?.size == 3)
        assertTrue(bikeList[0].devolutions?.size == 3)
    }
}