package app.igormatos.botaprarodar.domain.model.community

import app.igormatos.botaprarodar.common.extensions.convertToList
import app.igormatos.botaprarodar.utils.communityListResponseStub
import app.igormatos.botaprarodar.utils.communityMapResponseStub
import app.igormatos.botaprarodar.utils.nullCommunityResponseItemMapStub
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
    @DisplayName("Given a Community List Response")
    inner class CommunityDataMapper {

        @Test
        fun `When Response has null items, should return a Community List with default values`() {
            val communityResponse = nullCommunityResponseItemMapStub()

            val communityMapped = communityMapper.mapCommunityResponseToCommunity(communityResponse)

            for (index in communityResponse.toList().indices) {
                assertTrue(communityMapped[index].name.isEmpty())
                assertTrue(communityMapped[index].description.isEmpty())
                assertTrue(communityMapped[index].address.isEmpty())
                assertTrue(communityMapped[index].org_email.isEmpty())
                assertTrue(communityMapped[index].org_name.isEmpty())
                assertTrue(communityMapped[index].id.isEmpty())
            }
        }

        @Test
        fun `When Response has complete items, should return a Community List with same data`() {
            val communityResponse = communityMapResponseStub()

            val communityMapped = communityMapper.mapCommunityResponseToCommunity(communityResponse)

            for (index in communityResponse.convertToList().indices) {
                assertEquals(communityResponse[index]?.name, communityMapped[index].name)
                assertEquals(
                    communityResponse[index]?.description,
                    communityMapped[index].description
                )
                assertEquals(communityResponse[index]?.address, communityMapped[index].address)
                assertEquals(communityResponse[index]?.orgEmail, communityMapped[index].org_email)
                assertEquals(communityResponse[index]?.orgName, communityMapped[index].org_name)
                assertEquals(communityResponse[index]?.id, communityMapped[index].id)
            }
        }
    }
}

