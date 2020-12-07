package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.CommunityApiService
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.utils.communityListResponseStub
import app.igormatos.botaprarodar.utils.completeCommunityBodyStub
import app.igormatos.botaprarodar.utils.mappedCommunityListStub
import io.mockk.coEvery
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertThrows
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

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
                apiServiceMock.addCommunity(any())
            } returns apiReturn

            val repositoryReturn = runBlocking {
                communityRepository.addCommunity(
                    completeCommunityBodyStub()
                )
            }

            assertEquals(apiReturn.name, repositoryReturn)
        }

        @Test
        fun `When something goes wrong, should throws an Exception`() {

            val apiExceptionReturn = Exception()

            coEvery {
                apiServiceMock.addCommunity(any())
            } throws apiExceptionReturn

            assertThrows(Exception::class.java) {
                runBlocking {
                    communityRepository.addCommunity(
                        completeCommunityBodyStub()
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

            val apiReturn = communityListResponseStub()

            val mapperReturn = mappedCommunityListStub()

            coEvery {
                apiServiceMock.getCommunities()
            } returns apiReturn

            every {
                mapperMock.mapCommunityResponseToCommunity(apiReturn)
            } returns mapperReturn

            val communityListResult = runBlocking { communityRepository.getCommunities() }

            assertEquals(mapperReturn, communityListResult)
        }

        @Test
        fun `When something goes wrong in Api Service, should throws an Exception`() {

            val exception = Exception()

            coEvery {
                apiServiceMock.getCommunities()
            } throws exception

            assertThrows(Exception::class.java) {
                runBlocking { communityRepository.getCommunities() }
            }
        }

        @Test
        fun `When something goes wrong in Mapper, should throws an Exception`() {

            val exception = Exception()

            every {
                mapperMock.mapCommunityResponseToCommunity(
                    communityListResponseStub()
                )
            } throws exception

            assertThrows(Exception::class.java) {
                runBlocking { communityRepository.getCommunities() }
            }

        }
    }
}