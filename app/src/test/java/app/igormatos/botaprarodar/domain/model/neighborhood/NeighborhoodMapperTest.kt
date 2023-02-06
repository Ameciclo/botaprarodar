package app.igormatos.botaprarodar.domain.model.neighborhood

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NeighborhoodMapperTest {

    private val mapper = NeighborhoodMapper()

    @Test
    internal fun `should convert data`() {
        val neighborhoods = listOf(NeighborhoodRequest("0", "Test"))

        val names = mapper.transform(neighborhoods)

        assertEquals(listOf("Test"), names)
    }
}