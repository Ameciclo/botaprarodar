package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.repository.BicycleRepository
import app.igormatos.botaprarodar.domain.model.Bicycle
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
@DisplayName("Given AddNewBicycleUseCase")
internal class AddNewBicycleUseCaseTest {
    @InjectMockKs
    private lateinit var userCase: AddNewBicycleUseCase
    @MockK
    private lateinit var repository: BicycleRepository

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Nested
    @DisplayName("WHEN do post for create new bicycle")
    inner class AddNewBicycle {

        @Test
        fun `should create new bicycle and return simple result with string`() = runBlocking() {
            coEvery { repository.addNewBicycle(any(), any()) } returns "Created new bicycle"

            val responseResult = userCase.addNewBicycle("100", buildBicycle()) as SimpleResult.Success

            assertEquals("Created new bicycle", responseResult.data)
        }

        @Test
        fun `should return simple result with exception`() = runBlocking {
            val exceptionResult = Exception()
            coEvery { repository.addNewBicycle(any(), any()) } throws exceptionResult

            val responseResult = userCase.addNewBicycle("100", buildBicycle())

            assertTrue(responseResult is SimpleResult.Error)
            assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
        }

    }

    fun buildBicycle(): Bicycle {
        return Bicycle().apply {
            name = "Bicycle"
            order_number = 123
            serial_number = "123serial"
            photo_path = "http://bla.com"
            photo_thumbnail_path = "http://bla.com"
        }
    }

}