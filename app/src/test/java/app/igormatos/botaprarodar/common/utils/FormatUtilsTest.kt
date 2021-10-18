package app.igormatos.botaprarodar.common.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FormatUtilsTest {

    @Test
    fun `given an string param, when call formatAsJSONValidType() should return an string with quotes`() {
        val anyString = "anyString"
        val result = formatAsJSONValidType(anyString)

        assertEquals("\"$anyString\"", result)
    }
}