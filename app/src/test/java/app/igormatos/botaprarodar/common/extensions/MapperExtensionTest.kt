package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.utils.mapOfBikesRequest
import app.igormatos.botaprarodar.utils.mapOfWithdraws
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class MapperExtensionTest {

    @Test
    fun `given a Map object, when call convertToList() should return an object list`() {
        val list = mapOfWithdraws.convertToList()

        assertTrue(list.size == 3)
        assertEquals(list[0].id, "111")
        assertEquals(list[1].id, "222")
        assertEquals(list[2].id, "333")
    }

    @Test
    fun `given a Map of BikeRequest, when call convertMapperToBikeList() should return a Bike list`() {
        val list = mapOfBikesRequest.convertMapperToBikeList()

        assertTrue(list.size == 5)
        assertEquals(list[0].withdraws?.size, 3)
        assertEquals(list[0].devolutions?.size, 3)
        assertEquals(list[1].withdraws, null)
        assertNotNull(list[1].id)
    }
}