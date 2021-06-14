package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SelectCommunityUseCaseTest {

    private lateinit var useCase: SelectCommunityUseCase
    private lateinit var adminUseCase: AdminUseCase
    private lateinit var communityUseCase: CommunityUseCase

    @BeforeEach
    fun setup() {
        adminUseCase = mockk()
        communityUseCase = mockk()
        useCase = SelectCommunityUseCase(adminUseCase, communityUseCase)
    }

    @Test
    fun `should return SelectCommunityState Success with UserInfoState Admin when loadCommunitiesByAdmin execute with success and user is admin`() {
        runBlocking {
            // arrange
            val expectedCommunities = listOf(Community())
            val idUserAdmin = "1"
            val email = "email@email.com"

            coEvery {
                adminUseCase.isAdmin(idUserAdmin)
            } returns AdminState.IsAdmin

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Success(expectedCommunities)

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(idUserAdmin, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Success)
            val convertedResultState = resultState as SelectCommunityState.Success
            assertTrue(convertedResultState.userInfoState is UserInfoState.Admin)
            val convertedUserInfoState = resultState.userInfoState as UserInfoState.Admin
            assertEquals(convertedUserInfoState.communities, expectedCommunities)
        }
    }

    @Test
    fun `should return SelectCommunityState Success with UserInfoState NotIsAdmin when loadCommunitiesByAdmin execute with success and the user is not an admin but has a linked community`() {
        runBlocking {
            // arrange
            val idUserNotAdmin = "2"
            val email = "email@email.com"
            val expectedCommunities = listOf(Community(org_email = email))

            coEvery {
                adminUseCase.isAdmin(idUserNotAdmin)
            } returns AdminState.NotIsAdmin

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Success(expectedCommunities)

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(idUserNotAdmin, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Success)
            val convertedResultState = resultState as SelectCommunityState.Success
            assertTrue(convertedResultState.userInfoState is UserInfoState.NotAdmin)
            val convertedUserInfoState = resultState.userInfoState as UserInfoState.NotAdmin
            assertEquals(convertedUserInfoState.communities, expectedCommunities)
        }
    }

    @Test
    fun `should return SelectCommunityState Success with UserInfoState NotAdminWithoutCommunities when loadCommunitiesByAdmin execute with success and the user is not an admin does not have a linked community`() {
        runBlocking {
            // arrange
            val idUserNotAdmin = "2"
            val email = "email@email.com"
            val communities = listOf(Community())

            coEvery {
                adminUseCase.isAdmin(idUserNotAdmin)
            } returns AdminState.NotIsAdmin

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Success(communities)

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(idUserNotAdmin, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Success)
            val convertedResultState = resultState as SelectCommunityState.Success
            assertTrue(convertedResultState.userInfoState is UserInfoState.NotAdminWithoutCommunities)
        }
    }

    @Test
    fun `should return SelectCommunityState Error with NETWORK when adminUseCase execute without network connection`() {
        runBlocking {
            // arrange
            val id = "2"
            val email = "email@email.com"
            val expectedErrorType = BprErrorType.NETWORK

            coEvery {
                adminUseCase.isAdmin(id)
            } returns AdminState.Error(expectedErrorType)

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Success(listOf())

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(id, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Error)
            val convertedResultState = resultState as SelectCommunityState.Error
            assertEquals(expectedErrorType, convertedResultState.error)
        }
    }

    @Test
    fun `should return SelectCommunityState Error with UNKNOWN when adminUseCase execute with unmapped exception`() {
        runBlocking {
            // arrange
            val id = "2"
            val email = "email@email.com"
            val expectedErrorType = BprErrorType.UNKNOWN

            coEvery {
                adminUseCase.isAdmin(id)
            } returns AdminState.Error(expectedErrorType)

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Success(listOf())

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(id, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Error)
            val convertedResultState = resultState as SelectCommunityState.Error
            assertEquals(expectedErrorType, convertedResultState.error)
        }
    }

    @Test
    fun `should return SelectCommunityState Error with NETWORK when communityUseCase execute without network connection`() {
        runBlocking {
            // arrange
            val id = "2"
            val email = "email@email.com"
            val expectedErrorType = BprErrorType.NETWORK

            coEvery {
                adminUseCase.isAdmin(id)
            } returns AdminState.IsAdmin

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Error(expectedErrorType)

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(id, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Error)
            val convertedResultState = resultState as SelectCommunityState.Error
            assertEquals(expectedErrorType, convertedResultState.error)
        }
    }

    @Test
    fun `should return SelectCommunityState Error with UNKNOWN when communityUseCase execute with unmapped exception`() {
        runBlocking {
            // arrange
            val id = "2"
            val email = "email@email.com"
            val expectedErrorType = BprErrorType.UNKNOWN

            coEvery {
                adminUseCase.isAdmin(id)
            } returns AdminState.IsAdmin

            coEvery {
                communityUseCase.getCommunitiesPreview()
            } returns CommunityState.Error(expectedErrorType)

            // action
            val resultState: SelectCommunityState =
                useCase.loadCommunitiesByAdmin(id, email)

            // assert
            assertTrue(resultState is SelectCommunityState.Error)
            val convertedResultState = resultState as SelectCommunityState.Error
            assertEquals(expectedErrorType, convertedResultState.error)
        }
    }
}