package app.igormatos.botaprarodar.domain.model.community

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CommunityMapperTest {

    lateinit var communityMapper: CommunityMapper

    @BeforeEach
    fun setUp() {
        communityMapper = CommunityMapper()
    }

    @Nested
    @DisplayName("Given a Community Response with null properties, When Community Response has been mapped to Community")
    inner class NullCommunityDataMapper {

        @Test
        fun `should return a Community with default values`() {
            val communityResponse = listOf(
                CommunityResponse(
                    address = null,
                    description = null,
                    name = null,
                    orgEmail = null,
                    orgName = null,
                    id = null,
                    createdDate = null
                )
            )

            val communityMapped = communityMapper.mapCommunityResponseToCommunity(communityResponse)

            for (index in communityResponse.indices) {
                assertTrue(communityMapped[index].name.isEmpty())
                assertTrue(communityMapped[index].description.isEmpty())
                assertTrue(communityMapped[index].address.isEmpty())
                assertTrue(communityMapped[index].org_email.isEmpty())
                assertTrue(communityMapped[index].org_name.isEmpty())
                assertTrue(communityMapped[index].id.isEmpty())
            }
        }

    }

    @Nested
    @DisplayName("Given a Community Response with complete data, When Community Response has been mapped to Community")
    inner class CompleteCommunityDataMapper {

        @Test
        fun `should return a Community with same data`() {
            val communityResponse = listOf(
                CommunityResponse(
                    address = "Rua Teste 123",
                    description = "Descrição teste",
                    name = "Nome teste",
                    orgEmail = "teste@teste.com",
                    orgName = "Nome Responsável",
                    id = "id",
                    createdDate = null
                )
            )

            val communityMapped = communityMapper.mapCommunityResponseToCommunity(communityResponse)

            for (index in communityResponse.indices) {
                assertEquals(communityResponse[index].name, communityMapped[index].name)
                assertEquals(communityResponse[index].description, communityMapped[index].description)
                assertEquals(communityResponse[index].address, communityMapped[index].address)
                assertEquals(communityResponse[index].orgEmail, communityMapped[index].org_email)
                assertEquals(communityResponse[index].orgName, communityMapped[index].org_name)
                assertEquals(communityResponse[index].id, communityMapped[index].id)
            }
        }
    }
}

