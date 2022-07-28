package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.utils.communityMapResponseStub
import app.igormatos.botaprarodar.utils.completeCommunityRequestStub
import app.igormatos.botaprarodar.utils.mappedCommunityListStub
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

class CommunityRepositoryTest {

    lateinit var communityRepository: CommunityRepository

    val apiServiceMock = mockk<CommunityApiService>()

    val mapperMock = mockk<CommunityMapper>()

    @BeforeEach
    fun setUp() {
        communityRepository = CommunityRepository(apiServiceMock, mapperMock)
    }

    @Nested
    @DisplayName("Given an add new Community request")
    inner class CommunityInsert {

        @Test
        fun `When the response is a success, should return key of the new Community`() {

            val apiReturn = AddDataResponse("FirebaseKey")

            coEvery {
                apiServiceMock.addCommunity(any(), any())
            } returns apiReturn

            val repositoryReturn = runBlocking {
                communityRepository.addCommunity(
                    completeCommunityRequestStub()
                )
            }

            assertEquals(apiReturn.name, repositoryReturn)
        }

        @Test
        fun `When something goes wrong, should throws an Exception`() {

            val apiExceptionReturn = Exception()

            coEvery {
                apiServiceMock.addCommunity(any(), any())
            } throws apiExceptionReturn

            assertThrows(Exception::class.java) {
                runBlocking {
                    communityRepository.addCommunity(
                        completeCommunityRequestStub()
                    )
                }
            }
        }
    }

    @Nested
    @DisplayName("Given a Community list request")
    inner class CommunityListRetrieve {

        @Test
        fun `When the response is a success, should return a mapped list of Community`() {

            val apiReturn = communityMapResponseStub()

            val mapperReturn = mappedCommunityListStub()

            coEvery {
                apiServiceMock.getCommunities()
            } returns apiReturn

            every {
                mapperMock.mapCommunityResponseToCommunity(apiReturn)
            } returns mapperReturn

            val communityListResult = runBlocking { communityRepository.getCommunitiesPreview() }

            assertEquals(mapperReturn, communityListResult)
        }

        @Test
        fun `should return Community list when communityApiService execute with success`() {
            runBlocking {
                val communityMapResponse = communityMapResponseStub()
                val expectedCommunityListResponse = listOf<Community>()
                // arrange
                coEvery {
                    apiServiceMock.getCommunities()
                } returns communityMapResponse

                every {
                    mapperMock.mapCommunityResponseToCommunity(communityMapResponse)
                } returns expectedCommunityListResponse

                // action
                val response: List<Community> = communityRepository.getCommunitiesPreview()

                // assert
                assertEquals(expectedCommunityListResponse, response)
            }
        }
    }
}