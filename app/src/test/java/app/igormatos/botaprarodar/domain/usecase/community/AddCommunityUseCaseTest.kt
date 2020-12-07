package app.igormatos.botaprarodar.domain.usecase.community

import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityBody
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AddCommunityUseCaseTest {

    lateinit var addCommunityUseCase: AddCommunityUseCase

    val repositoryMock = mockk<CommunityRepository>()

    val newCommunity = CommunityBody()

    @BeforeEach
    fun setUp() {
        addCommunityUseCase = AddCommunityUseCase(repositoryMock)
    }

    @Nested
    @DisplayName("Given a Community insert request")
    inner class NewCommunityInsert {

        @Test
        fun `When the response is a success, should return a Simple Result with the Community's key`() {

            val repositoryReturn = "FirebaseKey"

            coEvery {
                repositoryMock.addCommunity(newCommunity)
            } returns repositoryReturn

            val insertResult = runBlocking {
                addCommunityUseCase.addNewCommunity(newCommunity) as SimpleResult.Success
            }

            assertEquals(repositoryReturn, insertResult.data)
        }

        @Test
        fun `When something goes wrong, should return a Simple Result with an Exception`() {

            val repositoryExceptionReturn = Exception()

            coEvery {
                repositoryMock.addCommunity(newCommunity)
            } throws repositoryExceptionReturn

            val insertExceptionResult = runBlocking {
                addCommunityUseCase.addNewCommunity(newCommunity)  as SimpleResult.Error
            }

            assertEquals(repositoryExceptionReturn, insertExceptionResult.exception)
        }

    }

}