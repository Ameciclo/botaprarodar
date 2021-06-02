package app.igormatos.botaprarodar.presentation.login.selectCommunity.community

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.Community
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommunityUseCaseTest {

    private lateinit var communityUseCase: CommunityUseCase
    private lateinit var communityRepository: CommunityRepository

    @BeforeEach
    fun setup() {
        communityRepository = mockk()
        communityUseCase = CommunityUseCase(communityRepository)
    }

    @Test
    fun `should return CommunityState Success when repository execute with success`() {
        runBlocking {
            // arrange
            val communities = listOf<Community>()

            coEvery {
                communityRepository.getCommunitiesPreview()
            } returns communities

            // action
            val resultState: CommunityState = communityUseCase.getCommunitiesPreview()

            // assert
            assertTrue(resultState is CommunityState.Success)
            val resultStateSuccess = resultState as CommunityState.Success
            assertEquals(communities, resultStateSuccess.communities)
        }
    }

    @Test
    fun `should return CommunityState with NETWORK error when repository execute without network connection`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.NETWORK

            coEvery {
                communityRepository.getCommunitiesPreview()
            } throws UserAdminErrorException.AdminNetwork

            // action
            val resultState: CommunityState = communityUseCase.getCommunitiesPreview()

            // assert
            assertTrue(resultState is CommunityState.Error)
            val resultStateError = resultState as CommunityState.Error
            assertEquals(expectedErrorType, resultStateError.type)
        }
    }

    @Test
    fun `should return CommunityState with UNKNOWN error when repository execute without unmapped exception`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.UNKNOWN

            coEvery {
                communityRepository.getCommunitiesPreview()
            } throws Exception()

            // action
            val resultState: CommunityState = communityUseCase.getCommunitiesPreview()

            // assert
            assertTrue(resultState is CommunityState.Error)
            val resultStateError = resultState as CommunityState.Error
            assertEquals(expectedErrorType, resultStateError.type)
        }
    }
}