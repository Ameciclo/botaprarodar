package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.buildMapStringAndBicycle
import app.igormatos.botaprarodar.data.repository.BikeRepository
import com.brunotmgomes.ui.SimpleResult
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.Exception

@ExtendWith(MockKExtension::class)
@DisplayName("Given BicyclesListUseCase")
internal class BicyclesListUseCaseTest {
    @InjectMockKs
    private lateinit var userCase: BicyclesListUseCase
    @MockK
    private lateinit var repository: BikeRepository

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Nested
    @DisplayName("WHEN do get bicycles list")
    inner class BicyclesList {

        @Test
        fun `should return simple result success with list contain 3 bicycles`() = runBlocking {
            coEvery { repository.getBicycles(any()) } returns buildMapStringAndBicycle(3)

            val listResult = userCase.list("10")

            assertEquals(3, (listResult as SimpleResult.Success).data.size)
            assertEquals("bicycle 3", listResult.data[0].name)
            assertEquals("bicycle 2", listResult.data[1].name)
            assertEquals("bicycle 1", listResult.data[2].name)
        }

        @Test
        fun `should return simple result error with exception`() = runBlocking {
            val exceptionResult = Exception()
            coEvery { repository.getBicycles(any()) } throws exceptionResult

            val listResult = userCase.list("10")

            assertTrue(listResult is SimpleResult.Error)
            assertEquals(exceptionResult, (listResult as SimpleResult.Error).exception)
        }

    }

}